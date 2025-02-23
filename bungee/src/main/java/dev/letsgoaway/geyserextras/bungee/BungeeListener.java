package dev.letsgoaway.geyserextras.bungee;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(ServerConnectEvent ev) {
        GeyserExtrasBungee.CORE.onJavaPlayerJoin(ev.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent ev) {
        GeyserExtrasBungee.CORE.onJavaPlayerLeave(ev.getPlayer().getUniqueId());
    }
}
