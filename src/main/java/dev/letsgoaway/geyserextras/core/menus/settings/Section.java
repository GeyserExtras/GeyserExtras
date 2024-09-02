package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.menus.SettingsMenu;
import org.geysermc.geyser.session.GeyserSession;

public class Section {
    public void build(BedrockForm menu, GeyserSession session, ExtrasPlayer player) {

    }

    public BedrockForm menu(GeyserSession session, ExtrasPlayer player) {
        BedrockForm menu = new BedrockForm();
        build(menu, session, player);
        return menu;
    }
}
