package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.TickMath;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class BungeeTickUtil implements TickUtil {

    @Override
    public void runIn(long ticks, Runnable func, float tickrate) {
        ProxyServer.getInstance().getScheduler().schedule(GeyserExtrasBungee.BUNGEE, func, TickMath.toNanos(tickrate)*ticks, TimeUnit.NANOSECONDS);
    }

    @Override
    public void runSync(Runnable func, float tickrate) {
        ProxyServer.getInstance().getScheduler().schedule(GeyserExtrasBungee.BUNGEE, func, TickMath.toNanos(tickrate), TimeUnit.NANOSECONDS);
    }
}
