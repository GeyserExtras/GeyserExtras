package dev.letsgoaway.geyserextras.form.elements;

import dev.letsgoaway.geyserextras.form.FormComponentType;
import dev.letsgoaway.geyserextras.form.FormElement;
import org.geysermc.cumulus.component.Component;
import org.geysermc.cumulus.component.DropdownComponent;

import java.util.List;
import java.util.function.Consumer;

public class Dropdown extends FormElement {
    public String title;
    public Consumer<String> onSubmit;
    public List<String> options;
    public String defaultOption;

    public Dropdown(String title, List<String> options, String defaultOption, Consumer<String> onSubmit) {
        this.title = title;
        this.options = options;
        this.defaultOption = defaultOption;
        this.onSubmit = onSubmit;
    }

    @Override
    public FormComponentType getType() {
        return FormComponentType.DROPDOWN;
    }

    @Override
    public void resultRecieved(Object... args) {
        this.onSubmit.accept(options.get((int) args[0]));
    }

    @Override
    public Component getComponent() {
        return DropdownComponent.of(title, options, options.indexOf(defaultOption));
    }
}
