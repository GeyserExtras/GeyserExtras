package dev.letsgoaway.geyserextras.spigot.form.elements;

import dev.letsgoaway.geyserextras.spigot.form.FormComponentType;
import dev.letsgoaway.geyserextras.spigot.form.FormElement;
import org.geysermc.cumulus.component.Component;
import org.geysermc.cumulus.component.ToggleComponent;

import java.util.function.Consumer;

public class Toggle extends FormElement {
    public String title;
    public boolean defaultValue;

    public Consumer<Boolean> onSubmit;

    public Toggle(String title, boolean defaultValue, Consumer<Boolean> onSubmit) {
        this.title = title;
        this.defaultValue = defaultValue;
        this.onSubmit = onSubmit;
    }


    @Override
    public FormComponentType getType() {
        return FormComponentType.TOGGLE;
    }

    @Override
    public void resultRecieved(Object... args) {
        this.onSubmit.accept((boolean) args[0]);
    }

    @Override
    public Component getComponent() {
        return ToggleComponent.of(title, defaultValue);
    }
}


