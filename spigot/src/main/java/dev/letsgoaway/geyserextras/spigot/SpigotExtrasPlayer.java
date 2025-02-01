package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;

public class SpigotExtrasPlayer extends ExtrasPlayer {
    public Player player;
    public SpigotExtrasPlayer(GeyserConnection connection) {
        super(connection);
    }

    @Override
    public void startGame() {
        super.startGame();
        player = Bukkit.getPlayer(getJavaUUID());
    }

    @Override
    public void tick() {
        super.tick();
    }
}
