package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.util.SettingsUtils;

public enum Menus {
    GEYSER_SETTINGS,
    GE_SETTINGS;

    public CustomForm open(ExtrasPlayer player) {
        switch (this) {
            case GEYSER_SETTINGS -> {
                return SettingsUtils.buildForm(player.getSession());
            }
            case GE_SETTINGS -> {
                return new SettingsMenu().create(player).build();
            }
        }
        return SettingsUtils.buildForm(player.getSession());
    }
}
