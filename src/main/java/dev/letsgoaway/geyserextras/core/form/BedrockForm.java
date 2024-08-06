package dev.letsgoaway.geyserextras.core.form;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Setter;
import org.geysermc.cumulus.form.CustomForm;

import java.util.ArrayList;
import java.util.List;

public class BedrockForm {
    private final CustomForm.Builder customForm;

    private final List<FormElement> elements;

    @Setter
    private String title = "";

    public BedrockForm() {
        customForm = CustomForm.builder();
        elements = new ArrayList<>();
    }

    public BedrockForm add(FormElement elem) {
        elements.add(elem);
        return this;
    }

    public CustomForm create(ExtrasPlayer player) {
        customForm.title(this.title);
        for (FormElement elem : elements) {
            customForm.component(elem.getComponent());
        }
        customForm.validResultHandler(response -> {
            int i = 0;
            response.includeLabels(true);
            while (response.isNextPresent()) {
                FormElement elem = elements.get(i);
                switch (elem.getType()) {
                    case DROPDOWN:
                        elem.resultRecieved(response.asDropdown(i));
                        break;
                    case INPUT:
                        elem.resultRecieved(response.asInput(i));
                        break;
                    case LABEL:
                        break;
                    case SLIDER:
                        elem.resultRecieved(response.asSlider(i));
                        break;
                    case STEPSLIDER:
                        elem.resultRecieved(response.asStepSlider(i));
                        break;
                    case TOGGLE:
                        elem.resultRecieved(response.asToggle(i));
                        break;
                }
                i++;
                response.next();
            }
            onSubmit();
        });

        customForm.closedOrInvalidResultHandler(this::onClose);

        return customForm.build();
    }

    public void onSubmit() {
    }

    public void onClose() {
    }
}
