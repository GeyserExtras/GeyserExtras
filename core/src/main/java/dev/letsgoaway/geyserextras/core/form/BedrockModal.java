package dev.letsgoaway.geyserextras.core.form;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.cumulus.form.ModalForm;

import java.util.function.Consumer;

public class BedrockModal {
    @Setter
    private String title = "";
    @Setter
    private String content = "";
    @Setter
    private String yesText = BedrockLocale.GUI.YES;
    @Setter
    private String noText = BedrockLocale.GUI.NO;

    public ModalForm create(ExtrasPlayer player) {
        return ModalForm.builder()
                .title(title)
                .content(content)
                .button1(yesText)
                .button2(noText)
                .validResultHandler((response) -> {
                    onSubmit(player, response.clickedFirst());
                })
                .closedOrInvalidResultHandler(()->{
                    onClose(player);
                })
                .build();
    }

    public void onSubmit(ExtrasPlayer player, boolean accepted) {}

    public void onClose(ExtrasPlayer player) {}

}

