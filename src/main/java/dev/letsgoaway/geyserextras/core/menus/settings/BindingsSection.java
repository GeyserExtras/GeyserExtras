package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
import dev.letsgoaway.geyserextras.core.menus.SettingsAndBindingsMenu;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.CooldownUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class BindingsSection {
    public static void build(SettingsAndBindingsMenu menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new Label(ChatColor.BOLD + "Bindings\n"));

        HashMap<String, Action> actionNames = new HashMap<>();
        for (Action action : Action.values()) {
            actionNames.put(action.getLocaleString(player), action);
        }
        for (Remappable binding : Remappable.values()) {
            menu.add(new Dropdown(binding.getLocaleString(player),
                    new ArrayList<>(actionNames.keySet()),
                    player.getPreferences().getAction(binding).getLocaleString(player), (str) -> {
                player.getPreferences().setAction(binding, actionNames.get(str));
            }));
        }
    }
}
