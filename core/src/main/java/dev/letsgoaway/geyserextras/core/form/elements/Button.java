package dev.letsgoaway.geyserextras.core.form.elements;

import dev.letsgoaway.geyserextras.core.form.FormComponentType;
import dev.letsgoaway.geyserextras.core.form.FormElement;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.cumulus.component.Component;
import org.geysermc.cumulus.util.FormImage;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class Button extends FormElement {
    public String text;
    public FormImage.Type type;
    public String data;
    public Runnable onSelect;

    public Button(String text, FormImage.Type type, String data, Runnable onSelect) {
        this.text = text;
        this.type = type;
        this.data = data;
        this.onSelect = onSelect;
    }

    public Button(String text, Runnable onSelect) {
        this.text = text;
        this.type = null;
        this.data = null;
        this.onSelect = onSelect;
    }

    @Override
    public FormComponentType getType() {
        return FormComponentType.BUTTON;
    }

    @Override
    public void resultRecieved(Object... args) {

    }

    public ButtonComponent get() {
        if (this.type != null && this.data != null) {
            return ButtonComponent.of(this.text, this.type, this.data);
        } else {
            return ButtonComponent.of(this.text);
        }
    }

    @Override
    public Component getComponent() {
        SERVER.warn("Buttons cannot be used in BedrockForm's! Returning a label.");
        return new Label(this.text).getComponent();
    }
}
