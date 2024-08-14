package dev.letsgoaway.geyserextras.core.form;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Setter;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.util.FormImage;

import java.util.ArrayList;
import java.util.List;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class BedrockForm {
    private final CustomForm.Builder customForm;

    private final List<FormElement> elements;

    @Setter
    private String title = "";

    public BedrockForm() {
        customForm = CustomForm.builder();
        elements = new ArrayList<>();
    }

    public void add(FormElement elem) {
        elements.add(elem);
    }

    public CustomForm.Builder create(ExtrasPlayer player) {
        customForm.icon(FormImage.Type.URL, "https://cdn.modrinth.com/data/kOfJBurB/2592d098bd31c083365e1c8d831446220995f1d7.png");
        customForm.title(this.title);
        for (FormElement elem : elements) {
            customForm.component(elem.getComponent());
        }
        customForm.validResultHandler((response) -> {
            int i = 0;
            response.includeLabels(true);
            for (FormElement element : elements) {
                switch (element.getType()) {
                    case DROPDOWN -> {
                        element.resultRecieved(response.asDropdown(i));
                    }
                    case INPUT -> {
                        element.resultRecieved(response.asInput(i));
                    }
                    case LABEL -> {
                        element.resultRecieved((Object) null);
                    }
                    case SLIDER -> {
                        element.resultRecieved(response.asSlider(i));
                    }
                    case STEPSLIDER -> {
                        element.resultRecieved(response.asStepSlider(i));
                    }
                    case TOGGLE -> {
                        element.resultRecieved(response.asToggle(i));
                    }
                    default -> {
                    }
                }
                i++;
            }
            onSubmit();
        });

        customForm.closedResultHandler(this::onClose);
        customForm.invalidResultHandler(() -> {
            SERVER.warn("ERROR: FORM RESPONSE BY " + player.getSession().getClientData().getUsername() + " WAS INVALID!");
        });
        return customForm;
    }

    public void onSubmit() {
    }

    public void onClose() {
    }
}
