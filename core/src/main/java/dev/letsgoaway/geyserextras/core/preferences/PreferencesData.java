package dev.letsgoaway.geyserextras.core.preferences;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.menus.Menus;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.util.CooldownUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;
import static dev.letsgoaway.geyserextras.core.cache.Cache.JSON_MAPPER;

public class PreferencesData {
    @JsonIgnore
    public static Path PREFERENCES_PATH;
    @JsonIgnore
    private static PreferencesData DEFAULT;
    @JsonIgnore
    private final ExtrasPlayer player;
    @JsonIgnore
    private final GeyserSession session;
    public CooldownUtils.CooldownType cooldownType = CooldownUtils.CooldownType.TITLE;
    public boolean showCoordinates = false;

    public boolean advancedTooltips = false;

    public boolean customSkullSkins = false;

    public HashMap<Remappable, Action> remappableActionMap;
    @Getter
    @Setter
    private boolean adjustCooldownWithPing = true;
    @Getter
    @Setter
    private float indicatorUpdateRate = 60f;
    @Getter
    @Setter
    private Menus settingsMenuForm = Menus.GE_SETTINGS;
    @Getter
    @Setter
    private boolean enableDoubleClickShortcut = true;
    @Getter
    @Setter
    private int doubleClickMS = 200;
    @Getter
    @Setter
    private boolean promptOnLinks = true;
    @Getter
    private Perspectives lockedPerspective = Perspectives.OFF;
    @Getter
    @Setter
    private List<UUID> selectedPacks = new ArrayList<>();
    @Getter
    @Setter
    private boolean sendSystemToasts = true;

    public PreferencesData(ExtrasPlayer player) {
        this.player = player;
        this.session = player.getSession();
        remappableActionMap = new HashMap<>();
        showCoordinates = GeyserImpl.getInstance().getConfig().isShowCoordinates();
    }


    public PreferencesData() {
        this.player = null;
        this.session = null;
        remappableActionMap = new HashMap<>();
        showCoordinates = GeyserImpl.getInstance().getConfig().isShowCoordinates();
    }

    public static void init() {
        PREFERENCES_PATH = SERVER.getPluginFolder().resolve("preferences/");
        try {
            Files.createDirectories(PreferencesData.PREFERENCES_PATH);
        } catch (Exception ignored) {
        }

        JavaPreferencesData.init();

        Path defaultPath = PREFERENCES_PATH.resolve("default.json");
        DEFAULT = new PreferencesData();
        try {
            if (defaultPath.toFile().exists()) {
                FileInputStream data = new FileInputStream(defaultPath.toFile());
                // Copy from because defaults might be different
                DEFAULT.copyFrom(JSON_MAPPER.convertValue(JSON_MAPPER.readTree(data.readAllBytes()), PreferencesData.class));
            }
            JSON_MAPPER.writeValue(defaultPath.toFile(), DEFAULT);
        } catch (Exception e) {
            SERVER.warn("Could not load custom default settings, new players will recieve factory\ndefault's, no changes have been made to default.json.\n" + e.getLocalizedMessage());
        }
    }

    public void runAction(Remappable binding) {
        if (!isDefault(binding)) {
            if (remappableActionMap.containsKey(binding)) {
                remappableActionMap.get(binding).run(player);
            }
        } else {
            switch (binding) {
                case OPEN_INVENTORY -> Action.OPEN_INVENTORY.run(player);
                default -> {
                }
            }
        }
    }

    public Action getAction(Remappable binding) {
        Action action = remappableActionMap.get(binding);
        return action == null ? Action.DEFAULT : action;
    }

    public void update() {
        if (session != null) {
            this.cooldownType = session.getPreferencesCache().getCooldownPreference();
            this.showCoordinates = session.getPreferencesCache().isPrefersShowCoordinates();
            this.advancedTooltips = session.isAdvancedTooltips();
            this.customSkullSkins = session.getPreferencesCache().showCustomSkulls();
        }
    }

    public void save() {
        this.update();
        if (GE.getConfig().isEnableGeyserExtrasMenu()) {
            new Thread(() -> {
                try {
                    if (!JSON_MAPPER.writeValueAsString(this).equals(JSON_MAPPER.writeValueAsString(DEFAULT))) {
                        JSON_MAPPER.writeValue(player.getUserPrefs(), this);
                    }
                } catch (IOException e) {
                    SERVER.warn("Could not save data for player " + player.getBedrockXUID() + "\n" + e.getLocalizedMessage());
                }
            }).start();
        }
    }

    public void load() {
        if (player.getUserPrefs().exists() && GE.getConfig().isEnableGeyserExtrasMenu()) {
            try {
                FileInputStream data = new FileInputStream(player.getUserPrefs());
                // Copy from because session would be null
                this.copyFrom(JSON_MAPPER.convertValue(JSON_MAPPER.readTree(data.readAllBytes()), PreferencesData.class));
            } catch (Exception e) {
                SERVER.warn("Could not load data for player " + player.getBedrockXUID() + ", restoring to default for them.\n" + e.getLocalizedMessage());

                this.copyFrom(DEFAULT);
            }
        } else {
            this.copyFrom(DEFAULT);
        }
        this.onLoad();
    }

    public void onLoad() {
        if (session != null) {
            session.getPreferencesCache().setCooldownPreference(this.cooldownType);
            session.getPreferencesCache().setPrefersShowCoordinates(this.showCoordinates);
            session.setAdvancedTooltips(this.advancedTooltips);
            if (player.isLoggedIn()) {
                session.getInventoryTranslator().updateInventory(session, session.getPlayerInventory());
            }
            session.getPreferencesCache().setPrefersCustomSkulls(this.customSkullSkins);
        }
    }

    // TODO: figure out literally any better way to do this
    public void copyFrom(PreferencesData data) {
        this.cooldownType = data.cooldownType;
        this.adjustCooldownWithPing = data.adjustCooldownWithPing;
        this.showCoordinates = data.showCoordinates;
        this.advancedTooltips = data.advancedTooltips;
        this.customSkullSkins = data.customSkullSkins;
        this.remappableActionMap = data.remappableActionMap;
        this.indicatorUpdateRate = data.indicatorUpdateRate;
        this.settingsMenuForm = data.settingsMenuForm;
        this.enableDoubleClickShortcut = data.enableDoubleClickShortcut;
        this.doubleClickMS = data.doubleClickMS;
        this.lockedPerspective = data.lockedPerspective;
        this.selectedPacks = data.selectedPacks;
        this.sendSystemToasts = data.sendSystemToasts;
    }

    public Action setAction(Remappable binding, Action action) {
        return remappableActionMap.put(binding, action);
    }

    public boolean isDefault(Remappable binding) {
        return remappableActionMap.get(binding) == null || remappableActionMap.get(binding) == Action.DEFAULT;
    }

    public void setLockedPerspective(Perspectives perspective) {
        this.lockedPerspective = perspective;
        if (session != null) {
            if (perspective.equals(Perspectives.OFF)) {
                session.camera().clearCameraInstructions();
                return;
            }
            // It wont equal null because the only one that can be null is OFF which we already handled
            assert perspective.getGeyser() != null;
            session.camera().forceCameraPerspective(perspective.getGeyser());
        }
    }
}
