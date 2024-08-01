package dev.letsgoaway.geyserextras.core;

public class TickMath {
    public static long toNanos(float tickrate){
        return Math.round(Math.ceil(1000000000L / tickrate));
    }
}
