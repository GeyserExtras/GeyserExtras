package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.TickMath;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.util.concurrent.TimeUnit;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class BungeeExtrasPlayer extends ExtrasPlayer {

    public BungeeExtrasPlayer(GeyserConnection connection) {
        super(connection);
        SERVER.getTickUtil().runIn(1L, this::tick, this.tickrate);
    }

    @Override
    public void tick() {
        super.tick();
        ProxyServer.getInstance().getScheduler().schedule(GeyserExtrasBungee.BUNGEE, this::tick, TickMath.toNanos(this.tickrate), TimeUnit.NANOSECONDS);
        SERVER.getTickUtil().runIn(1L, this::tick, this.tickrate);
    }
}
