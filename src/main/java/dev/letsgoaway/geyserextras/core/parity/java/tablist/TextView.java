package dev.letsgoaway.geyserextras.core.parity.java.tablist;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockModal;
import lombok.Setter;
import org.geysermc.cumulus.form.ModalForm;

public class TextView extends BedrockModal {

    @Override
    public ModalForm create(ExtrasPlayer player) {
        setTitle("");
        setContent(player.getTabListData().getHeader() + "\n" + player.getTabListData().getFooter());
        setYesText("Refresh");
        setNoText("Close");
        return super.create(player);
    }

    @Override
    public void onSubmit(ExtrasPlayer player, boolean accepted) {
        super.onSubmit(player, accepted);
        if (accepted) {
            player.sendForm(new TextView());
        }
    }
}
