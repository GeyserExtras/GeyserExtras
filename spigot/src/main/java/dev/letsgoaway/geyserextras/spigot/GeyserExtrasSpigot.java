package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import dev.letsgoaway.geyserextras.core.preferences.JavaPreferencesData;
import dev.letsgoaway.geyserextras.core.utils.TickUtil;
import dev.letsgoaway.geyserextras.spigot.config.GeyserExtrasSpigotConfig;
import dev.letsgoaway.geyserextras.spigot.config.SpigotConfigLoader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.nio.file.Path;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class GeyserExtrasSpigot extends JavaPlugin implements Server {
    public static GeyserExtras CORE;

    public static GeyserExtrasSpigot SPIGOT;

    private static SpigotTickUtil spigotTickUtil;

    @Getter
    @Setter
    private GeyserExtrasSpigotConfig platformConfig;

    public GeyserExtrasSpigot() {
        ServerType.type = ServerType.SPIGOT;
        SPIGOT = this;
        spigotTickUtil = new SpigotTickUtil();
    }

    @Override
    public void onEnable() {
        SpigotCommandHandler handler = new SpigotCommandHandler();
        this.getCommand("geyserextras").setExecutor(handler);
        this.getCommand("platformlist").setExecutor(handler);
        this.getCommand("playerlist").setExecutor(handler);
        this.getCommand("emotechat").setExecutor(handler);
        this.getServer().getPluginManager().registerEvents(new SpigotListener(), this);
        CORE = new GeyserExtras(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            CORE.serverTick();
        }, 0L, 0L);
    }

    @Override
    public void onConfigLoad() {
        SpigotConfigLoader.load();
    }

    @Override
    public void onDisable() {
        CORE.autoReconnectAll();
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

    @Override
    public void sendEmoteChat(ExtrasPlayer player, String emoteChat) {
        SpigotExtrasPlayer spigotPlayer = (SpigotExtrasPlayer) player;
        spigotTickUtil.runSync(() -> {
            for (Player playerNear : spigotPlayer.player.getWorld().getEntitiesByClass(Player.class)) {
                boolean isBedrockPlayer = GE.geyserApi.isBedrockPlayer(playerNear.getUniqueId());
                if (isBedrockPlayer) continue;

                JavaPreferencesData userPrefs = GE.getJavaPreferencesData(playerNear.getUniqueId());
                if (userPrefs != null && userPrefs.muteEmoteChat) continue;

                if (EmoteUtils.EMOTE_DISTANCE >= spigotPlayer.player.getLocation().distance(playerNear.getLocation())) {
                    playerNear.sendMessage(ChatColor.translateAlternateColorCodes('§', emoteChat));
                }
            }
        }, player);
    }

    @Override
    public void sendRawMessage(UUID javaPlayer, String message) {
        Bukkit.getPlayer(javaPlayer).sendRawMessage(message);
    }

    @Override
    public void sendMessage(UUID javaPlayer, String message) {
        Bukkit.getPlayer(javaPlayer).sendMessage(ChatColor.translateAlternateColorCodes('§', message));
    }
}
