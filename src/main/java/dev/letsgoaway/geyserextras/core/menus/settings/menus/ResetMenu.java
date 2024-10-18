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

public class ResetMenu extends BedrockMenu {
    @Override

    public SimpleForm create(ExtrasPlayer player) {
        setHeader("Resetting your settings will require reconnecting.\nContinue?");

        add(new Button(BedrockLocale.CONTROLLER.RESET_TO_DEFAULT, ()->{
            player.getUserPrefs().delete();
            player.reconnect();
        }));

        add(new Button(BedrockLocale.CONTROLLER.CANCEL, ()->{
                onClose(player);
        }));


        return super.create(player);
    }

    @Override
    public void onClose(ExtrasPlayer player){
        player.sendForm(new SettingsMenu());
    }
}
