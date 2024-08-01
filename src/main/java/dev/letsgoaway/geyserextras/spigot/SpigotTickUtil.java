package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.TickUtil;
import org.bukkit.Bukkit;

public class SpigotTickUtil implements TickUtil {

    @Override
    public void runIn(long ticks, Runnable func, float tickrate) {
        Bukkit.getScheduler().runTaskLater(GeyserExtrasSpigot.SPIGOT, func, ticks);
    }

    @Override
    public void runSync(Runnable func) {
        Bukkit.getScheduler().runTask(GeyserExtrasSpigot.SPIGOT, func);
    }
}
