package dev.letsgoaway.geyserextras.core.menus.settings.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.menus.settings.Settings;
import dev.letsgoaway.geyserextras.core.menus.settings.sections.*;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;

import java.util.List;

public class InGameSettingsMenu extends BedrockForm {
    @Override
    public CustomForm.Builder create(ExtrasPlayer player) {
        setTitle(player.translateGE("ge.settings"));
        GeyserSession session = player.getSession();
        List<Section> sections = Settings.list();

        add(new Label(player.translateGE("ge.settings.reset.ingame")));

        for (Section section : sections) {
            section.create(this, session, player);
        }

        return super.create(player);
    }

    @Override
    public void onSubmit(ExtrasPlayer player) {
        player.getPreferences().save();
    }
}
