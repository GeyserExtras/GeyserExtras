package dev.letsgoaway.geyserextras.extension;

import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.TickMath;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExtensionExtrasPlayer extends ExtrasPlayer {
    private ScheduledFuture<?> tickThread = null;
    private ScheduledFuture<?> combatTickThread = null;

    public ExtensionExtrasPlayer(GeyserConnection connection) {
        super(connection);
    }

    @Override
    public void startGame() {
        super.startGame();
        this.setTickingState(20.0f);
    }

    @Override
    public void setTickingState(float tickrate) {
        super.setTickingState(tickrate);
        tickThread = session.getEventLoop().scheduleAtFixedRate(this::tick, TickMath.toNanos(tickrate), TickMath.toNanos(tickrate), TimeUnit.NANOSECONDS);
        combatTickThread = session.getEventLoop().scheduleAtFixedRate(() -> {
            if (Config.customCoolDownEnabled) {
                getCooldownHandler().tick();
            }
        }, TickMath.toNanos(60), TickMath.toNanos(tickrate), TimeUnit.NANOSECONDS);
    }

    @Override
    public void onDisconnect() {
        tickThread.cancel(true);
        tickThread = null;
        combatTickThread.cancel(true);
        combatTickThread = null;
        super.onDisconnect();
    }
}
