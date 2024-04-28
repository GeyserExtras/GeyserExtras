package dev.letsgoaway.geyserextras.form;

import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.GeyserExtras;
import dev.letsgoaway.geyserextras.form.elements.Button;
import org.geysermc.cumulus.form.SimpleForm;

import java.util.ArrayList;
import java.util.List;

public class BedrockContextMenu {
    public SimpleForm.Builder simpleForm = SimpleForm.builder();
    public Runnable onClose = null;
    List<Button> buttons = new ArrayList<>();

    public BedrockContextMenu(String title, String header) {
        simpleForm.title(title);
        simpleForm.content(header);
    }

    public BedrockContextMenu(String title) {
        simpleForm.title(title);
        simpleForm.content("");
    }

    public BedrockContextMenu add(Button button) {
        buttons.add(button);
        return this;
    }

    public void show(BedrockPlayer bplayer) {
        for (Button button : buttons) {
            simpleForm.button(button.get());
        }
        simpleForm.validResultHandler(response -> {
            for (Button button : buttons) {
                if (response.clickedButtonId() == buttons.indexOf(button)) {
                    button.onSelect.run();
                }
            }
        });
        if (onClose != null) {
            simpleForm.closedOrInvalidResultHandler(onClose);
        }
        GeyserExtras.bedrockAPI.sendForm(bplayer.player.getUniqueId(), simpleForm.build());
    }
}
