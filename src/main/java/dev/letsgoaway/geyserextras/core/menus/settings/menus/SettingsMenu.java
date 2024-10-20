package dev.letsgoaway.geyserextras.core.menus.settings.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.menus.settings.Settings;
import dev.letsgoaway.geyserextras.core.menus.settings.sections.Section;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

public class SettingsMenu extends BedrockMenu {
    @Override

    public SimpleForm create(ExtrasPlayer player) {
        setTitle("GeyserExtras Settings");
        GeyserSession session = player.getSession();
        List<Section> sections = Settings.list();

        for (Section section : sections) {
            section.create(this, session, player);
        }

        add(new Button(BedrockLocale.CONTROLLER.RESET_TO_DEFAULT, () -> {
            player.sendForm(new ResetModal());
        }));

        return super.create(player);
    }
}
