package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.geysermc.geyser.api.connection.GeyserConnection;

public class SpigotExtrasPlayer extends ExtrasPlayer {
    public SpigotExtrasPlayer(GeyserConnection connection) {
        super(connection);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
