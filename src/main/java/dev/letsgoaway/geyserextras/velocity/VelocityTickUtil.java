package dev.letsgoaway.geyserextras.velocity;

import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.TickMath;

import java.util.concurrent.TimeUnit;

public class VelocityTickUtil implements TickUtil {

    @Override
    public void runIn(long ticks, Runnable func, ExtrasPlayer player) {
        GeyserExtrasVelocity.server.getScheduler()
                .buildTask(GeyserExtrasVelocity.VELOCITY, func)
                .delay(TickMath.toNanos(player.getTickrate()) * ticks, TimeUnit.NANOSECONDS)
                .schedule();
    }

    @Override
    public void runSync(Runnable func, ExtrasPlayer player) {
        GeyserExtrasVelocity.server.getScheduler()
                .buildTask(GeyserExtrasVelocity.VELOCITY, func)
                .delay(TickMath.toNanos(player.getTickrate()), TimeUnit.NANOSECONDS)
                .clearRepeat()
                .schedule();
    }
}
