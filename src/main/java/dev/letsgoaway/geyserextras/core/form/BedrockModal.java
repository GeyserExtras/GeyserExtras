package dev.letsgoaway.geyserextras.core.form;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
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
    private String yesText = "";
    @Setter
    private String noText = "";

    public ModalForm create(ExtrasPlayer player) {
        return ModalForm.builder()
                .title(title)
                .content(content)
                .button1(yesText)
                .button2(noText)
                .validResultHandler((response) -> {
                    onSubmit(player, response.clickedFirst());
                })
                .build();
    }

    public void onSubmit(ExtrasPlayer player, boolean accepted) {}
}

