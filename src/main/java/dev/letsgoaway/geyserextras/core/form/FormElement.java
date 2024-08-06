package dev.letsgoaway.geyserextras.core.form;

import org.geysermc.cumulus.component.Component;

public abstract class FormElement {
    public abstract FormComponentType getType();
    public abstract void resultRecieved(Object... args);
    public abstract Component getComponent();
}
