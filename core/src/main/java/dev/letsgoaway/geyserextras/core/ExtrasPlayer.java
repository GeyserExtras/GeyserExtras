package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.core.features.skinsaver.SkinSaver;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.BedrockModal;
import dev.letsgoaway.geyserextras.core.locale.GELocale;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import dev.letsgoaway.geyserextras.core.parity.java.combat.CooldownHandler;
import dev.letsgoaway.geyserextras.core.parity.java.menus.serverlinks.ServerLinksData;
import dev.letsgoaway.geyserextras.core.parity.java.menus.tablist.TabListData;
import dev.letsgoaway.geyserextras.core.preferences.PreferencesData;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import dev.letsgoaway.geyserextras.core.utils.StringUtils;
import dev.letsgoaway.geyserextras.core.utils.TickMath;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.bedrock.packet.AnimatePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerboundDiagnosticsPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket;
import org.cloudburstmc.protocol.bedrock.packet.ToastRequestPacket;
import org.geysermc.geyser.api.bedrock.camera.CameraPerspective;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.geysermc.geyser.level.JavaDimension;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.BossBar;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.DimensionUtils;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class ExtrasPlayer {
    @Getter
    private final CooldownHandler cooldownHandler;
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
    private ScheduledFuture<?> combatTickThread;
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

    public ExtrasPlayer(GeyserConnection connection) {
        this.session = (GeyserSession) connection;
        this.javaUUID = connection.javaUuid();
        this.bedrockXUID = connection.xuid();
        cooldownHandler = new CooldownHandler(this);
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

        // Update the cooldown at a faster rate for smoother animations at fast periods
        startCombatTickThread(60f);
        // Java UUID is null until login
        javaUUID = session.javaUuid();
    }

    private void createFpsBossBar() {
        long entityId = session.getEntityCache().getNextEntityId().incrementAndGet();
        fpsBossBar = new BossBar(session, entityId, Component.text(diagnostics != null ? "FPS: " + Math.round(diagnostics.getAvgFps()) : ""), 1.0f, 0, 1, 0);

        UUID bossbarID = UUID.randomUUID();
        session.getEntityCache().addBossBar(bossbarID, fpsBossBar);
    }


    public void startCombatTickThread(float updateRate) {
        getPreferences().setIndicatorUpdateRate(updateRate);
        if (combatTickThread != null) {
            combatTickThread.cancel(false);
        }
        combatTickThread = session.getTickEventLoop().scheduleAtFixedRate(() -> {
            if (GE.getConfig().isEnableCustomCooldown()) {
                getCooldownHandler().tick();
            }
        }, TickMath.toNanos(updateRate), TickMath.toNanos(updateRate), TimeUnit.NANOSECONDS);
    }

    public void onDisconnect() {
        if (combatTickThread != null) {
            combatTickThread.cancel(false);
            combatTickThread = null;
        }
        if (doubleClickShortcutFuture != null) {
            doubleClickShortcutFuture.cancel(false);
            doubleClickShortcutFuture = null;
        }
        tabListData.getPlayers().clear();
    }

    public void reconnect() {
        String[] data = session.getClientData().getServerAddress().split(":");
        String address = data[0];
        int port = Integer.parseInt(data[1]);
        session.transfer(address, port);
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
        if (GE.getConfig().isDisablePaperDoll()) {
            session.camera().hideElement(GuiElement.PAPER_DOLL);
        }

        if (session.getDimensionType().isNetherLike()) {
            if (session.camera().fogEffects().contains(DimensionUtils.BEDROCK_FOG_HELL)) {
                session.camera().removeFog(DimensionUtils.BEDROCK_FOG_HELL);
            }
        }

        if (preferences.isShowFPS() && diagnostics != null && fpsBossBar == null) {
            createFpsBossBar();
        }
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        SetTitlePacket timesPacket = new SetTitlePacket();
        timesPacket.setText("");
        timesPacket.setType(SetTitlePacket.Type.TIMES);
        timesPacket.setFadeInTime(fadeIn);
        timesPacket.setStayTime(stay);
        timesPacket.setFadeOutTime(fadeOut);
        timesPacket.setXuid("");
        timesPacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(timesPacket);
        SetTitlePacket titlePacket = new SetTitlePacket();
        titlePacket.setType(SetTitlePacket.Type.TITLE);
        titlePacket.setText(title.isEmpty() ? " " : title);
        titlePacket.setXuid("");
        titlePacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(titlePacket);
        SetTitlePacket subtitlePacket = new SetTitlePacket();
        subtitlePacket.setType(SetTitlePacket.Type.SUBTITLE);
        subtitlePacket.setText(subtitle);
        subtitlePacket.setXuid("");
        subtitlePacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(subtitlePacket);
    }

    public void sendActionbarTitle(String title) {
        SetTitlePacket titlePacket = new SetTitlePacket();
        titlePacket.setType(SetTitlePacket.Type.ACTIONBAR_JSON);
        titlePacket.setText("{ \"rawtext\": [ { \"text\":\"" + StringUtils.escape(title) + "\" } ] }");
        titlePacket.setXuid("");
        titlePacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(titlePacket);
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
            toastPacket.setTitle(title);
            toastPacket.setContent(description);
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
        animatePacket.setRuntimeEntityId(session.getPlayerEntity().getGeyserId());
        animatePacket.setAction(AnimatePacket.Action.SWING_ARM);
        session.sendUpstreamPacket(animatePacket);
    }

    public String translate(String lang) {
        return MinecraftLocale.getLocaleString(lang, session.locale());
    }

    public String translateGE(String lang) {
        return GELocale.translate(lang, session.locale());
    }
}
