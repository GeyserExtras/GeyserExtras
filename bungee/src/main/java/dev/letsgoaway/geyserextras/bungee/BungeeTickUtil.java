package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.utils.TickMath;
import dev.letsgoaway.geyserextras.core.utils.TickUtil;

import java.util.concurrent.TimeUnit;

public class BungeeTickUtil implements TickUtil {

    @Override
    public void runIn(long ticks, Runnable func, ExtrasPlayer player) {
        if (player instanceof BungeeExtrasPlayer bungeeExtrasPlayer) {
            bungeeExtrasPlayer.scheduler.schedule(GeyserExtrasBungee.BUNGEE, func, TickMath.toNanos(player.getTickrate()) * ticks, TimeUnit.NANOSECONDS);
        }
    }

    @Override
    public void runSync(Runnable func, ExtrasPlayer player) {
        if (player instanceof BungeeExtrasPlayer bungeeExtrasPlayer) {
            bungeeExtrasPlayer.scheduler.schedule(GeyserExtrasBungee.BUNGEE, func, TickMath.toNanos(player.getTickrate()), TimeUnit.NANOSECONDS);
        }
    }
}
