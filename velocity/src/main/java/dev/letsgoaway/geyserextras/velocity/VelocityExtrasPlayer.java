package dev.letsgoaway.geyserextras.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.utils.TickMath;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.util.concurrent.TimeUnit;

import static com.velocitypowered.api.scheduler.TaskStatus.SCHEDULED;

public class VelocityExtrasPlayer extends ExtrasPlayer {
    public Player player;
    Scheduler.TaskBuilder builder;
    ScheduledTask task;

    Scheduler scheduler;

    public VelocityExtrasPlayer(GeyserConnection connection) {
        super(connection);
        this.scheduler = GeyserExtrasVelocity.server.getScheduler();
    }

    @Override
    public void startGame() {
        super.startGame();
        this.setTickingState(20.0f);

        GeyserExtrasVelocity.server.getPlayer(getJavaUUID()).ifPresent(value -> this.player = value);
    }

    @Override
    public void setTickingState(float tickrate) {
        super.setTickingState(tickrate);
        if (builder == null) {
            builder = scheduler.buildTask(GeyserExtrasVelocity.VELOCITY, this::tick);
        }
        if (task != null && task.status().equals(SCHEDULED)) {
            task.cancel();
            task = null;
        }
        builder.clearRepeat();
        builder.repeat(TickMath.toNanos(tickrate), TimeUnit.NANOSECONDS);
        task = builder.schedule();
    }
}
