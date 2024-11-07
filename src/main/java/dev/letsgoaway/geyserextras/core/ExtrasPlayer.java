package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.core.features.skinsaver.SkinSaver;
import dev.letsgoaway.geyserextras.core.locale.GELocale;
import dev.letsgoaway.geyserextras.core.parity.java.menus.serverlinks.ServerLinksData;
import dev.letsgoaway.geyserextras.core.preferences.PreferencesData;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.BedrockModal;
import dev.letsgoaway.geyserextras.core.parity.java.combat.CooldownHandler;
import dev.letsgoaway.geyserextras.core.parity.java.shield.ShieldUtils;
import dev.letsgoaway.geyserextras.core.parity.java.menus.tablist.TabListData;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import dev.letsgoaway.geyserextras.core.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.geysermc.api.util.BedrockPlatform;
import org.geysermc.api.util.InputMode;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.DimensionUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class ExtrasPlayer {
    @Getter
    private UUID javaUUID;

    @Getter
    private String bedrockXUID;

    @Setter
    private List<UUID> emotesList;

    @Getter
    public GeyserSession session;

    @Getter
    private final CooldownHandler cooldownHandler;

    @Getter
    private final TabListData tabListData;

    @Getter
    private final ServerLinksData serverLinksData;


    @Getter
    private final PreferencesData preferences;

    @Getter
    private ScheduledFuture<?> combatTickThread;

    @Getter
    @Setter
    private ScheduledFuture<?> doubleClickShortcutFuture;
    @Getter
    private File userPrefs;

    public ExtrasPlayer(GeyserConnection connection) {
        this.session = (GeyserSession) connection;
        this.javaUUID = connection.javaUuid();
        this.bedrockXUID = connection.xuid();
        cooldownHandler = new CooldownHandler(this);
        tabListData = new TabListData(this);
        serverLinksData = new ServerLinksData(this);
        preferences = new PreferencesData(this);
        emotesList = List.of();
        userPrefs = PreferencesData.PREFERENCES_PATH.resolve(bedrockXUID + ".json").toFile();
    }

    @Getter
    private boolean loggedIn = false;

    public void startGame() {
        loggedIn = true;
        sendToast(translateGE("ge.welcome_toast.line1"), translateGE("ge.welcome_toast.line2"));
        if (GE.getConfig().isEnableSkinSaving()) {
            SkinSaver.save(this);
        }

        preferences.load();

        // Update the cooldown at a faster rate for smoother animations at fast periods
        startCombatTickThread(60f);
        // Java UUID is null until login
        javaUUID = session.javaUuid();
    }

    public void startCombatTickThread(float updateRate) {
        getPreferences().setIndicatorUpdateRate(updateRate);
        if (combatTickThread != null) {
            combatTickThread.cancel(false);
        }
        combatTickThread = session.getEventLoop().scheduleAtFixedRate(() -> {
            if (GE.getConfig().isEnableCustomCooldown()) {
                getCooldownHandler().tick();
            }
        }, TickMath.toNanos(updateRate), TickMath.toNanos(updateRate), TimeUnit.NANOSECONDS);
    }

    public void onDisconnect() {
        if (combatTickThread != null) {
            combatTickThread.cancel(true);
            combatTickThread = null;
        }
        if (doubleClickShortcutFuture != null) {
            doubleClickShortcutFuture.cancel(true);
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

    public void onEmoteEvent(ClientEmoteEvent ev) {
        int id = emotesList.indexOf(UUID.fromString(ev.emoteId()));

        if (id == -1) {
            SERVER.debugWarn("Emote with id: " + ev.emoteId() + " was not in emote list!");
            return;
        }

        preferences.getAction(Remappable.values()[id]).run(this);
    }

    @Setter
    @Getter
    public float tickrate = 20.0f;

    public long ticks = 0;

    // Used for the VR Quick-Menu double click action
    @Setter
    @Getter
    private float lastInventoryClickTime = 0;

    public void tick() {
        ticks++;
        if (GE.getConfig().isDisablePaperDoll()) {
            session.camera().hideElement(GuiElement.PAPER_DOLL);
        }
        if (GE.getConfig().isEnableToggleBlock()) {
            ShieldUtils.updateBlockSpeed(session);
        }
        if (session.getDimensionType().isNetherLike()) {
            if (session.camera().fogEffects().contains(DimensionUtils.BEDROCK_FOG_HELL)) {
                session.camera().removeFog(DimensionUtils.BEDROCK_FOG_HELL);
            }
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

    private static final List<BedrockPlatform> vrPlatforms = List.of(BedrockPlatform.GEARVR, BedrockPlatform.HOLOLENS);

    public boolean isVR() {
        return session.inputMode() == InputMode.VR || vrPlatforms.contains(session.platform());
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
