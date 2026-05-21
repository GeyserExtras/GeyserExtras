package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.core.features.skinsaver.SkinSaver;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.BedrockModal;
import dev.letsgoaway.geyserextras.core.locale.GELocale;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import dev.letsgoaway.geyserextras.core.parity.java.menus.serverlinks.ServerLinksData;
import dev.letsgoaway.geyserextras.core.parity.java.menus.tablist.TabListData;
import dev.letsgoaway.geyserextras.core.preferences.PreferencesData;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.utils.IdUtils;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.bedrock.packet.AnimatePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerboundDiagnosticsPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket;
import org.cloudburstmc.protocol.bedrock.packet.ToastRequestPacket;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.geysermc.geyser.level.JavaDimension;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.BossBar;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.DimensionUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

//
public class ExtrasPlayer {
    @Getter
    private final TabListData tabListData;
    @Getter
    private final ServerLinksData serverLinksData;
    @Getter
    private final PreferencesData preferences;
    @Getter
    public GeyserSession session;
    @Setter
    @Getter
    public float tickrate = 20.0f;
    public long ticks = 0;
    @Getter
    private UUID javaUUID;
    @Getter
    private String bedrockXUID;
    @Setter
    @Getter
    private ArrayList<UUID> emotesList;
    @Getter
    @Setter
    private ScheduledFuture<?> doubleClickShortcutFuture;
    @Getter
    private File userPrefs;
    @Getter
    @Setter
    private ServerboundDiagnosticsPacket diagnostics = null;
    @Setter
    @Getter
    private boolean packsUpdated = false;
    @Getter
    private boolean loggedIn = false;
    private Instant lastEmoteTime = Instant.now();

    // Used for the double click menu action
    @Setter
    @Getter
    private float lastInventoryClickTime = 0;

    // For emote chat on platforms where we cant get the dimensions of other players
    @Getter
    private Map<Integer, JavaDimension> playerDimensionsMap;

    @Getter
    @Setter
    private BossBar fpsBossBar;

    @Setter
    @Getter
    private boolean emoting = false;

    @Getter
    private double averagePing = 0.0f;
    @Getter
    private long pingSample = 0;
    @Getter
    private long pingSampleSize = 0;
    @Getter
    private int lastPing = -1;

    public ExtrasPlayer(GeyserConnection connection) {
        this.session = (GeyserSession) connection;
        this.javaUUID = connection.javaUuid();
        this.bedrockXUID = connection.xuid();
        tabListData = new TabListData(this);
        serverLinksData = new ServerLinksData(this);
        preferences = new PreferencesData(this);
        emotesList = new ArrayList<>();
        userPrefs = PreferencesData.PREFERENCES_PATH.resolve(bedrockXUID + ".json").toFile();
        preferences.load();
        playerDimensionsMap = new HashMap<>();
    }

    public void startGame() {
        loggedIn = true;

        sendSystemToast(translateGE("ge.welcome_toast.line1"), translateGE("ge.welcome_toast.line2"));

        if (GE.getConfig().isEnableSkinSaving()) {
            SkinSaver.save(this);
        }

        // Java UUID is null until login
        javaUUID = session.javaUuid();
    }

    private UUID bossbarID;

    private void createFpsBossBar() {
        long entityId = session.getEntityCache().getNextEntityId().incrementAndGet();
        fpsBossBar = new BossBar(session, entityId, Component.text(getBossBarText()), 1.0f, 0, 1, 0);
        bossbarID = UUID.randomUUID();
        session.getEntityCache().addBossBar(bossbarID, fpsBossBar);
    }

    public String getBossBarText() {
        return diagnostics != null ? "FPS: " + Math.round(diagnostics.getAvgFps()) : "";
    }

    public void onDisconnect() {
        if (doubleClickShortcutFuture != null) {
            doubleClickShortcutFuture.cancel(false);
            doubleClickShortcutFuture = null;
        }
        tabListData.getPlayers().clear();
        if (fpsBossBar != null) {
            session.getEntityCache().removeBossBar(bossbarID);
            fpsBossBar = null;
        }
    }

    public void reconnect() {
        session.transfer(session.joinAddress(), session.joinPort());
    }


    public void hungerSprintCancel() {
        // todo: figure out how to recreate this option with geyser codebase
    }

    public void onEmoteEvent(ClientEmoteEvent ev) {
        UUID uuid = UUID.fromString(ev.emoteId());
        int id = emotesList.indexOf(uuid);

        if (id == -1) {
            SERVER.debugWarn("Emote with id: " + ev.emoteId() + " was not in emote list!");
            return;
        }

        if (preferences.isDefault(Remappable.values()[id])) {
            String emoteChat = EmoteUtils.getEmoteChatString(uuid, this);

            if (emoteChat != null && Duration.between(lastEmoteTime, Instant.now()).toMillis() >= 3000) {
                SERVER.sendEmoteChat(this, emoteChat);
                lastEmoteTime = Instant.now();
            }
        } else {
            preferences.getAction(Remappable.values()[id]).run(this);
        }

    }

    public void tick() {
        ticks++;
        if (GE.getConfig().isDisablePaperDoll() && !session.camera().isHudElementHidden(GuiElement.PAPER_DOLL)) {
            session.camera().hideElement(GuiElement.PAPER_DOLL);
        }


        if (session.getDimensionType().isNetherLike() && session.camera().fogEffects().contains(DimensionUtils.BEDROCK_FOG_HELL)) {
            session.camera().removeFog(DimensionUtils.BEDROCK_FOG_HELL);
        }


        if (fpsBossBar == null && preferences.isShowFPS() && diagnostics != null) {
            createFpsBossBar();
        }

        calculateAveragePing();
    }

    private void calculateAveragePing() {
        int ping = session.ping();
        if (ping != lastPing) {
            pingSample += ping;
            pingSampleSize++;
            lastPing = ping;
        }
        averagePing = (double) pingSample / pingSampleSize;
    }

    public void sendToast(String title, String description) {
        ToastRequestPacket toastPacket = new ToastRequestPacket();
        toastPacket.setTitle(title);
        toastPacket.setContent(description);
        session.sendUpstreamPacket(toastPacket);
    }

    public void sendSystemToast(String title, String description) {
        if (preferences.isSendSystemToasts() && GE.getConfig().isEnableGeyserExtrasMenu()) {
            ToastRequestPacket toastPacket = new ToastRequestPacket();
            toastPacket.setTitle((CharSequence) title);
            toastPacket.setContent((CharSequence) description);
            session.sendUpstreamPacket(toastPacket);
        }
    }

    public void sendMessage(String text) {
        session.sendMessage(text);
    }

    public void resetTitle() {
        SetTitlePacket titlePacket = new SetTitlePacket();
        titlePacket.setType(SetTitlePacket.Type.CLEAR);
        titlePacket.setText("");
        titlePacket.setXuid("");
        titlePacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(titlePacket);
    }

    public void sendForm(BedrockForm form) {
        if (IsAvailable.floodgate()) {
            org.geysermc.floodgate.api.FloodgateApi.getInstance().getPlayer(javaUUID).sendForm(form.create(this).build());
        } else {
            session.sendForm(form.create(this).build());
        }
    }

    public void sendForm(BedrockMenu form) {
        if (IsAvailable.floodgate()) {
            org.geysermc.floodgate.api.FloodgateApi.getInstance().getPlayer(javaUUID).sendForm(form.create(this));
        } else {
            session.sendForm(form.create(this));
        }
    }

    public void sendForm(BedrockModal form) {
        if (IsAvailable.floodgate()) {
            org.geysermc.floodgate.api.FloodgateApi.getInstance().getPlayer(javaUUID).sendForm(form.create(this));
        } else {
            session.sendForm(form.create(this));
        }
    }

    public void setTickingState(float tickrate) {
        this.tickrate = tickrate;
    }

    public void swingArm() {
        AnimatePacket animatePacket = new AnimatePacket();

        long runtimeId = session.getPlayerEntity().getEntityId();
        animatePacket.setRuntimeEntityId(runtimeId);
        animatePacket.setAction(AnimatePacket.Action.SWING_ARM);

        session.sendUpstreamPacket(animatePacket);
    }

    public String translate(String lang) {
        return MinecraftLocale.getLocaleString(lang, session.locale());
    }

    public String translateGE(String lang) {
        return GELocale.translate(lang, session.locale());
    }
    public String translateOtherwiseGE(String lang, String otherwise) {
        return GELocale.translateOtherwise(lang, otherwise, session.locale());
    }

    @Nullable
    public static ExtrasPlayer get(UUID javaUUID) {
        long XUID = IdUtils.getBedrockXUID(javaUUID);
        if (XUID != -1) {
            return get(XUID);
        }
        for (ExtrasPlayer player : GE.connections.values()) {
            if (player.getJavaUUID() != null && player.getJavaUUID().equals(javaUUID)) {
                return player;
            }
        }
        return null;
    }

    public static ExtrasPlayer get(GeyserConnection connection) {
        return get(connection.xuid());
    }

    public static ExtrasPlayer get(long XUID) {
        return get(String.valueOf(XUID));
    }

    public static ExtrasPlayer get(String XUID) {
        return GE.connections.get(XUID);
    }

    public static boolean exists(GeyserConnection connection) {
        return exists(connection.xuid());
    }

    public static boolean exists(long XUID) {
        return exists(String.valueOf(XUID));
    }

    public static boolean exists(String XUID) {
        return GE.connections.containsKey(XUID);
    }

}
