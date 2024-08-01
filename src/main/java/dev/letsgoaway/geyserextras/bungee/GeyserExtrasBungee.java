package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import net.md_5.bungee.api.plugin.Plugin;

public class GeyserExtrasBungee extends Plugin implements Server {
    public static GeyserExtras CORE;

    private static BungeeTickUtil bungeeTickUtil;
    public static Plugin BUNGEE;

    public GeyserExtrasBungee() {
        ServerType.type = ServerType.BUNGEE;
        BUNGEE = this;
        bungeeTickUtil = new BungeeTickUtil();
    }

    @Override
    public void onEnable() {

        CORE = new GeyserExtras(this);
    }

    @Override
    public TickUtil getTickUtil() {
        return bungeeTickUtil;
    }
}
