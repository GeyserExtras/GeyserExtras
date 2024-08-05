package dev.letsgoaway.geyserextras.extension;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.TickMath;
import io.netty.util.concurrent.ScheduledFuture;
import org.geysermc.geyser.api.connection.GeyserConnection;
import java.util.concurrent.TimeUnit;

public class ExtensionExtrasPlayer extends ExtrasPlayer {
    private ScheduledFuture<?> tickThread = null;
    public ExtensionExtrasPlayer(GeyserConnection connection) {
        super(connection);
    }

    @Override
    public void setTickingState(float tickrate) {
        super.setTickingState(tickrate);
        tickThread = getSession().getEventLoop().scheduleAtFixedRate(this::tick, TickMath.toNanos(tickrate), TickMath.toNanos(tickrate), TimeUnit.NANOSECONDS);
    }

    @Override
    public void onDisconnect(){
        super.onDisconnect();
        tickThread.cancel(false);
    }
}
