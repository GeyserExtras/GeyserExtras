package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.menus.settings.sections.*;

import java.util.List;

public class Settings {
    public static List<Section> list() {
        return List.of(
                new SettingsSection(),
                new MenuSection(),
                new HudSection(),
                new BindingsSection(),
                new DebugSection(),
                new CreditsSection()
        );
    }
}
