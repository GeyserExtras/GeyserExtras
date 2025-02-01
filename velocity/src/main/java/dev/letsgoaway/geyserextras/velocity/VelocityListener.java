package dev.letsgoaway.geyserextras.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;

public class VelocityListener {
    @Subscribe(order = PostOrder.LAST)
    public void onPlayerJoin(LoginEvent ev) {
        GeyserExtrasVelocity.CORE.onJavaPlayerJoin(ev.getPlayer().getUniqueId());
    }
    @Subscribe(order = PostOrder.LAST)
    public void onPlayerKick(KickedFromServerEvent ev) {
        GeyserExtrasVelocity.CORE.autoReconnectAll();
    }
    @Subscribe(order = PostOrder.LAST)
    public void onPlayerLeave(DisconnectEvent ev) {
        GeyserExtrasVelocity.CORE.onJavaPlayerLeave(ev.getPlayer().getUniqueId());
    }
}
