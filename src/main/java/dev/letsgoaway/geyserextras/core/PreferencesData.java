package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;

import java.util.HashMap;
import java.util.Map;

public class PreferencesData {
    private final ExtrasPlayer player;
    public HashMap<Remappable, Action> remappableActionMap;

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
}
