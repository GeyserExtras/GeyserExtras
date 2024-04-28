package dev.letsgoaway.geyserextras.form;

import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.GeyserExtras;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.cumulus.form.CustomForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BedrockForm {
    private CustomForm.Builder customForm;

    private List<FormElement> elements;
    public Runnable onSubmit = null;
    public Runnable onClose = null;
    public BedrockForm(String title) {
        customForm = CustomForm.builder();
        customForm.title(title);
        elements = new ArrayList<>();
    }
    public BedrockForm add(FormElement elem){
        elements.add(elem);
        return this;
    }

    public void show(BedrockPlayer bplayer){
        for (FormElement elem : elements){
            customForm.component(elem.getComponent());
        }
        customForm.validResultHandler(response -> {
           int i = 0;
           response.includeLabels(true);
           while (response.isNextPresent()){
               FormElement elem = elements.get(i);
               switch (elem.getType()){
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
            if (onSubmit != null) {
                onSubmit.run();
            }
        });
        if (onClose != null) {
            customForm.closedOrInvalidResultHandler(onClose);
        }
        GeyserExtras.bedrockAPI.sendForm(bplayer.player.getUniqueId(), customForm.build());
    }
}
