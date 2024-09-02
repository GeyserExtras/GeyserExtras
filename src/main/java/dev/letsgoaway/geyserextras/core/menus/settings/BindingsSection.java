package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class BindingsSection {
    public static void build(BedrockForm menu, ExtrasPlayer player) {
        menu.add(new SectionLabel(BedrockLocale.CONTROLLER.BINDINGS, ""));

        LinkedHashMap<String, Action> actionNames = new LinkedHashMap<>();
        for (Action action : Action.values()) {
            actionNames.put(action.translate(player), action);
        }
        for (Remappable binding : Remappable.values()) {
            menu.add(new Dropdown(binding.translate(player),
                    new ArrayList<>(actionNames.keySet()),
                    player.getPreferences().getAction(binding).translate(player), (str) -> {
                player.getPreferences().setAction(binding, actionNames.get(str));
            }));
        }
    }
}
