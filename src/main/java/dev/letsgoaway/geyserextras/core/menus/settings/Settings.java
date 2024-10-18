package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.menus.settings.sections.*;

import java.util.List;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class Settings {
    public static List<Section> list() {
        if (GE.getConfig().isDebugMode()) {
            return List.of(
                    new SettingsSection(),
                    new MenuSection(),
                    new HudSection(),
                    new BindingsSection(),
                    new DebugSection(),
                    new CreditsSection()
            );
        }
        else {
            return List.of(
                    new SettingsSection(),
                    new MenuSection(),
                    new HudSection(),
                    new BindingsSection(),
                    new CreditsSection()
            );
        }
    }
}
