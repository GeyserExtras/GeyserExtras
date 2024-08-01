package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        CORE = new GeyserExtras(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,()->{
            CORE.tick();
        }, 0L,0L);
    }

    @Override
    public TickUtil getTickUtil() {
        return spigotTickUtil;
    }
}
