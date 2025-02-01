package dev.letsgoaway.geyserextras.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpigotListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        GeyserExtrasSpigot.CORE.onJavaPlayerJoin(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    public void onPlayerLeave(Player player) {
        GeyserExtrasSpigot.CORE.onJavaPlayerLeave(player.getUniqueId());
    }

}
