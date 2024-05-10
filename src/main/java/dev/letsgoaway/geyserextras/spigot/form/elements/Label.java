package dev.letsgoaway.geyserextras.spigot.form.elements;

import dev.letsgoaway.geyserextras.spigot.form.FormComponentType;
import dev.letsgoaway.geyserextras.spigot.form.FormElement;
import org.geysermc.cumulus.component.Component;
import org.geysermc.cumulus.component.LabelComponent;

public class Label extends FormElement {
    public String text = "";
    public Label(String text){
        this.text = text;
    }
    @Override
    public FormComponentType getType() {
        return FormComponentType.LABEL;
    }

    @Override
    public void resultRecieved(Object... args) {
    }

    @Override
    public Component getComponent() {
        return LabelComponent.of(this.text);
    }
}
