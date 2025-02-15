package dev.letsgoaway.geyserextras.core.utils;

public class TickMath {
    public static long toNanos(float tickrate) {
        return Math.round(Math.ceil(1000000000L / tickrate));
    }

    public static float toMillis(float tickrate) {
        return 1000.0f / tickrate;
    }

}
