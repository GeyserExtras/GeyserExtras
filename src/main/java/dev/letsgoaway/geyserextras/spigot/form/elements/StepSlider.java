package dev.letsgoaway.geyserextras.spigot.form.elements;

import dev.letsgoaway.geyserextras.spigot.form.FormComponentType;
import dev.letsgoaway.geyserextras.spigot.form.FormElement;
import org.geysermc.cumulus.component.Component;
import org.geysermc.cumulus.component.StepSliderComponent;

import java.util.List;
import java.util.function.Consumer;

public class StepSlider extends FormElement {
    public String title = "";
    public List<String> steps;
    public String defaultStep;
    public Consumer<String> onResult;

    public StepSlider(String title, List<String> steps, String defaultStep, Consumer<String> onResult) {
        this.title = title;
        this.steps = steps;
        this.defaultStep = defaultStep;
        this.onResult = onResult;
    }

    public StepSlider(String title, List<String> steps, Consumer<String> onResult) {
        this.title = title;
        this.steps = steps;
        this.defaultStep = steps.getFirst();
        this.onResult = onResult;
    }

    @Override
    public FormComponentType getType() {
        return FormComponentType.STEPSLIDER;
    }

    @Override
    public void resultRecieved(Object... args) {
        onResult.accept(steps.get((int) args[0]));
    }

    @Override
    public Component getComponent() {
        return StepSliderComponent.of(title, steps, steps.indexOf(defaultStep));
    }
}
