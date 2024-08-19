package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.core.preferences.Perspectives;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.menus.Menus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class PreferencesData {
    private final ExtrasPlayer player;
    public HashMap<Remappable, Action> remappableActionMap;

    @Getter
    @Setter
    private float indicatorUpdateRate = 60f;

    @Getter
    @Setter
    private Menus settingsMenuForm = Menus.GE_SETTINGS;

    @Getter
    @Setter
    private boolean enableDoubleClickForVRQuickMenu = true;

    @Getter
    @Setter
    private int vrMenuDoubleClickMS = 200;

    @Getter
    private Perspectives lockedPerspective = Perspectives.OFF;

    public PreferencesData(ExtrasPlayer player) {
        this.player = player;
        remappableActionMap = new HashMap<>();
    }

    public void runAction(Remappable binding) {
        if (remappableActionMap.containsKey(binding)) {
            remappableActionMap.get(binding).run(player);
        }
    }

    public Action getAction(Remappable binding) {
        Action action = remappableActionMap.get(binding);
        return action == null ? Action.DEFAULT : action;
    }

    public Action setAction(Remappable binding, Action action) {
        return remappableActionMap.put(binding, action);
    }

    public boolean isDefault(Remappable binding) {
        return remappableActionMap.get(binding) == null || remappableActionMap.get(binding) == Action.DEFAULT;
    }

    public void setLockedPerspective(Perspectives perspective) {
        this.lockedPerspective = perspective;
        if (perspective.equals(Perspectives.OFF)) {
            player.getSession().camera().clearCameraInstructions();
            return;
        }
        // It wont equal null because the only one that can be null is OFF which we already handled
        assert perspective.getGeyser() != null;
        player.getSession().camera().forceCameraPerspective(perspective.getGeyser());
    }
}
