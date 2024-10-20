package dev.letsgoaway.geyserextras.core.menus.settings.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockModal;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import org.geysermc.cumulus.form.ModalForm;

public class ResetModal extends BedrockModal {
    @Override

    public ModalForm create(ExtrasPlayer player) {
        setContent("Resetting your settings will require reconnecting.\n\n" + player.translate("mco.configure.world.leave.question.line2"));

        setYesText(BedrockLocale.CONTROLLER.RESET_TO_DEFAULT);
        setNoText(BedrockLocale.CONTROLLER.CANCEL);

        return super.create(player);
    }

    @Override
    public void onSubmit(ExtrasPlayer player, boolean accepted) {
        if (accepted) {
            new Thread(() -> {
                player.getUserPrefs().delete();
                player.reconnect();
            }).start();
        } else {
            onClose(player);
        }
    }

    @Override
    public void onClose(ExtrasPlayer player) {
        player.sendForm(new SettingsMenu());
    }
}
