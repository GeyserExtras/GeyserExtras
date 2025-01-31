package dev.letsgoaway.geyserextras.core.form;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import lombok.Setter;
import org.geysermc.cumulus.form.SimpleForm;

import java.util.ArrayList;
import java.util.List;

public class BedrockMenu {
    private SimpleForm.Builder simpleForm;
    private final List<Button> buttons;


    @Setter
    private String title = "";

    @Setter
    private String header = "";

    public BedrockMenu() {
        simpleForm = SimpleForm.builder();
        buttons = new ArrayList<>();
    }

    public void onClose(ExtrasPlayer player) {
    }

    public void onButtonClick(ExtrasPlayer player) {
    }

    public BedrockMenu add(Button button) {
        buttons.add(button);
        return this;
    }

    public SimpleForm create(ExtrasPlayer player) {
        simpleForm = SimpleForm.builder();
        simpleForm.title(this.title)
                .content(this.header);
        for (Button button : buttons) {
            simpleForm.button(button.get());
        }
        simpleForm.validResultHandler(response -> {
            for (Button button : buttons) {
                if (response.clickedButtonId() == buttons.indexOf(button)) {
                    button.onSelect.run();
                    onButtonClick(player);
                }
            }
        });

        simpleForm.closedOrInvalidResultHandler(() -> {
            onClose(player);
        });

        return simpleForm.build();
    }
}
