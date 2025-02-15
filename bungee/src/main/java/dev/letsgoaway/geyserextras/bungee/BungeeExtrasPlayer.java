package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import dev.letsgoaway.geyserextras.core.utils.TickMath;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.util.concurrent.TimeUnit;

public class BungeeExtrasPlayer extends ExtrasPlayer {
    public ProxiedPlayer player;

    public BungeeExtrasPlayer(GeyserConnection connection) {
        super(connection);
    }

    @Override
    public void startGame() {
        super.startGame();
        GeyserExtras.SERVER.getTickUtil().runIn(1L, this::tick, this);
        this.player = GeyserExtrasBungee.BUNGEE.getProxy().getPlayer(getJavaUUID());
    }

    @Override
    public void tick() {
        super.tick();
        GeyserExtrasBungee.BUNGEE.getProxy().getScheduler().schedule(GeyserExtrasBungee.BUNGEE, this::tick, TickMath.toNanos(this.tickrate), TimeUnit.NANOSECONDS);
        GeyserExtras.SERVER.getTickUtil().runIn(1L, this::tick, this);
    }
}
