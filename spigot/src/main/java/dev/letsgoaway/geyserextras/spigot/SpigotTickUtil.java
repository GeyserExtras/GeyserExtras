package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.core.utils.TickUtil;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.bukkit.Bukkit;

public class SpigotTickUtil implements TickUtil {

    @Override
    public void runIn(long ticks, Runnable func, ExtrasPlayer player) {
        Bukkit.getScheduler().runTaskLater(GeyserExtrasSpigot.SPIGOT, func, ticks);
    }

    @Override
    public void runSync(Runnable func, ExtrasPlayer player) {
        Bukkit.getScheduler().runTask(GeyserExtrasSpigot.SPIGOT, func);
    }
}
