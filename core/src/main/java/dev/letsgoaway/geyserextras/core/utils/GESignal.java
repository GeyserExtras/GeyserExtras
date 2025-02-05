package dev.letsgoaway.geyserextras.core.utils;

import java.util.ArrayList;
import java.util.List;

public class GESignal {
    private final List<Runnable> _once = new ArrayList<>();

    public void addOnce(Runnable func) {
        _once.add(func);
    }

    public void dispatch() {
        _once.forEach(Runnable::run);
        _once.clear();
    }
}
