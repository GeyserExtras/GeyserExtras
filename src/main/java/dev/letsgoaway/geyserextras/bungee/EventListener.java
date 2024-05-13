package dev.letsgoaway.geyserextras.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static dev.letsgoaway.geyserextras.bungee.GeyserExtras.fogChannel;

public class EventListener implements Listener {
    @EventHandler
    public void onPluginMessage(PluginMessageEvent ev) {
        if (ev.getTag().equals(fogChannel) && GeyserEventForwarder.enableNetherFog) {
            if (ev.getReceiver() instanceof ProxiedPlayer player) {
                String data = new String(ev.getData(), StandardCharsets.UTF_8);
                String fogID = data.substring(1);
                if (data.charAt(0) == 'r') {
                    Objects.requireNonNull(GeyserEventForwarder.api.connectionByUuid(player.getUniqueId())).camera().removeFog(fogID);
                } else {
                    Objects.requireNonNull(GeyserEventForwarder.api.connectionByUuid(player.getUniqueId())).camera().sendFog(fogID);
                }
            }
        }
    }
}
