package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.menus.SettingsMenu;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;

public class BindingsSection {
    public static void build(SettingsMenu menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new Label(ChatColor.BOLD + BedrockLocale.CONTROLLER.BINDINGS +"\n"));

        HashMap<String, Action> actionNames = new HashMap<>();
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
