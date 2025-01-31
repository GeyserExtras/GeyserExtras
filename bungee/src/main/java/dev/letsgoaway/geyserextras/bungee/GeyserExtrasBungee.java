package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import dev.letsgoaway.geyserextras.core.handlers.CommandHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.io.File;
import java.nio.file.Path;

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
        for (String label : CommandHandler.ids.keySet()){
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeCommandHandler(label));
        }
    }

    @Override
    public TickUtil getTickUtil() {
        return bungeeTickUtil;
    }

    @Override
    public ExtrasPlayer createPlayer(GeyserConnection connection) {
        return new BungeeExtrasPlayer(connection);
    }

    @Override
    public void log(String string) {
        this.getLogger().info(string);
    }

    @Override
    public void warn(String string) {
        this.getLogger().warning(string);
    }

    @Override
    public Path getPluginFolder() {
        return getDataFolder().toPath();
    }
}
