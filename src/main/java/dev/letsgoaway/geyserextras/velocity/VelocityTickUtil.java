package dev.letsgoaway.geyserextras.velocity;

import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.TickMath;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VelocityTickUtil implements TickUtil {

    @Override
    public void runIn(long ticks, Runnable func, float tickrate) {
        GeyserExtrasVelocity.server.getScheduler()
                .buildTask(GeyserExtrasVelocity.VELOCITY, func)
                .delay(TickMath.toNanos(tickrate) * ticks, TimeUnit.NANOSECONDS)
                .schedule();
    }

    @Override
    public void runSync(Runnable func, float tickrate) {
        GeyserExtrasVelocity.server.getScheduler()
                .buildTask(GeyserExtrasVelocity.VELOCITY, func)
                .delay(TickMath.toNanos(tickrate), TimeUnit.NANOSECONDS)
                .clearRepeat()
                .schedule();
    }
}
