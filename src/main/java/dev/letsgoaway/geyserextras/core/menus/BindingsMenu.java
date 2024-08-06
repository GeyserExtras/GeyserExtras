package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import org.geysermc.cumulus.form.CustomForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingsMenu extends BedrockForm {
    @Override
    public CustomForm create(ExtrasPlayer player) {
        HashMap<String, Action> actionNames = new HashMap<>();
        for (Action action : Action.values()) {
            actionNames.put(action.getLocaleString(player), action);
        }
        for (Remappable binding : Remappable.values()) {
            add(new Dropdown(binding.getLocaleString(player),
                    new ArrayList<>(actionNames.keySet()),
                    player.getPreferences().getAction(binding).getLocaleString(player), (str) -> {
                player.getPreferences().setAction(binding, actionNames.get(str));
            }));
        }

        return super.create(player);
    }
}
