package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.io.File;
import java.nio.file.Path;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class GeyserExtrasSpigot extends JavaPlugin implements Server {
    public static GeyserExtras CORE;

    public static GeyserExtrasSpigot SPIGOT;

    private static SpigotTickUtil spigotTickUtil;

    public GeyserExtrasSpigot() {
        ServerType.type = ServerType.SPIGOT;
        spigotTickUtil = new SpigotTickUtil();
    }

    @Override
    public void onEnable() {
        SpigotCommandHandler handler = new SpigotCommandHandler();
        this.getCommand("geyserextras").setExecutor(handler);
        this.getCommand("platformlist").setExecutor(handler);
        this.getCommand("playerlist").setExecutor(handler);
        this.getCommand("emotechat").setExecutor(handler);
        CORE = new GeyserExtras(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            CORE.serverTick();
        }, 0L, 0L);
    }

    @Override
    public TickUtil getTickUtil() {
        return spigotTickUtil;
    }

    @Override
    public ExtrasPlayer createPlayer(GeyserConnection connection) {
        return new SpigotExtrasPlayer(connection);
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
