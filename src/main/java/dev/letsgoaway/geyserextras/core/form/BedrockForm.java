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
            while (response.isNextPresent()) {

                FormElement elem = elements.get(i);
                SERVER.log(elem.getType().name());
                switch (elem.getType()) {
                    case DROPDOWN -> elem.resultRecieved(response.asDropdown(i));
                    case LABEL -> {}
                    case INPUT -> elem.resultRecieved(response.asInput(i));
                    case SLIDER -> elem.resultRecieved(response.asSlider(i));
                    case STEPSLIDER -> elem.resultRecieved(response.asStepSlider(i));
                    case TOGGLE -> elem.resultRecieved(response.asToggle(i));
                    default -> {}
                }
                i++;
                response.next();
            }
            onSubmit();
        });

        customForm.closedResultHandler(this::onClose);
        customForm.invalidResultHandler(()->{
           SERVER.warn("ERROR: FORM RESPONSE BY " + player.getSession().getClientData().getUsername() + " WAS INVALID!");
        });
        return customForm;
    }

    public void onSubmit() {
    }

    public void onClose() {
    }
}
