package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.cache.Cache;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.menus.SettingsMenu;
import org.geysermc.geyser.session.GeyserSession;

public class CreditsSection {
    public static void build(SettingsMenu menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new SectionLabel(BedrockLocale.OPTIONS.CREDITS, ""));

        menu.add(new Label(Cache.CREDITS_TEXT));
    }

}
