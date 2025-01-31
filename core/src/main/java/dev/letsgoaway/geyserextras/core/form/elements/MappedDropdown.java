package dev.letsgoaway.geyserextras.core.form.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MappedDropdown<T> extends Dropdown {
    private final Map<String, T> mappedOptions;
    private final Consumer<T> mappedOnSubmit;

    public MappedDropdown(String title, Map<String, T> options, T defaultOption, Consumer<T> mappedOnSubmit) {
        super(title, options.keySet().stream().toList(), null, null);
        this.mappedOptions = options;
        this.mappedOnSubmit = mappedOnSubmit;
        this.defaultOption = getDefaultOption(defaultOption);
        this.onSubmit = (s) -> {
            this.mappedOnSubmit.accept(mappedOptions.get(s));
        };
    }

    private String getDefaultOption(T option) {
        for (String str : mappedOptions.keySet()) {
            if (option.equals(mappedOptions.get(str))) {
                return str;
            }
        }
        return options.get(0);
    }
}
