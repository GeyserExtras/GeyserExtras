package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.*;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.menus.Menus;
import dev.letsgoaway.geyserextras.core.menus.SettingsMenu;
import org.geysermc.geyser.session.GeyserSession;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugSection {
    public static void build(SettingsMenu menu, GeyserSession session, ExtrasPlayer player) {

        menu.add(new SectionLabel(BedrockLocale.OPTIONS.DEBUG,
                "Don't change unless you know what you are doing!"));

        HashMap<String, Menus> menuNames = new HashMap<>();
        for (Menus menuOption : Menus.values()) {
            menuNames.put(menuOption.name(), menuOption);
        }
        menu.add(new Dropdown("Settings Menu Form:",
                new ArrayList<>(menuNames.keySet()),
                player.getPreferences().getSettingsMenuForm().name(),
                (str) -> player.getPreferences().setSettingsMenuForm(Menus.valueOf(str))
        ));
    }
}
