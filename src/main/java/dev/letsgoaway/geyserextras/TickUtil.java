package dev.letsgoaway.geyserextras;

public interface TickUtil {
    void runIn(long ticks, Runnable func, float tickrate);

    void runSync(Runnable func, float tickrate);
}
