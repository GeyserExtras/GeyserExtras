package dev.letsgoaway.geyserextras.spigot.form.elements;

import dev.letsgoaway.geyserextras.spigot.form.FormComponentType;
import dev.letsgoaway.geyserextras.spigot.form.FormElement;
import org.checkerframework.checker.index.qual.Positive;
import org.geysermc.cumulus.component.Component;
import org.geysermc.cumulus.component.SliderComponent;

import java.util.function.Consumer;

public class Slider extends FormElement {
    public String title = "";
    public float min;
    public float max;
    public float step;
    public float defaultValue;

    public Consumer<Float> onResult;

    public Slider(String title, float min, float max, @Positive float step, float defaultValue, Consumer<Float> onResult) {
        this.title = title;
        this.min = min;
        this.max = max;
        this.step = step;
        this.defaultValue = defaultValue;
        this.onResult = onResult;
    }

    public Slider(String title, float min, float max, @Positive float step, Consumer<Float> onResult) {
        this.title = title;
        this.min = min;
        this.max = max;
        this.step = step;
        this.defaultValue = max / 2;
        this.onResult = onResult;
    }

    @Override
    public FormComponentType getType() {
        return FormComponentType.SLIDER;
    }

    @Override
    public void resultRecieved(Object... args) {
        onResult.accept((float) args[0]);
    }

    @Override
    public Component getComponent() {
        return SliderComponent.of(this.title, this.min, this.max, this.step, this.defaultValue);
    }
}
