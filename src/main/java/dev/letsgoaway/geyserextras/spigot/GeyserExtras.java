package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.VersionConstants;
import dev.letsgoaway.geyserextras.spigot.api.APIType;
import dev.letsgoaway.geyserextras.spigot.commands.EmoteChatCommand;
import dev.letsgoaway.geyserextras.spigot.commands.GeyserExtrasCommand;
import dev.letsgoaway.geyserextras.spigot.commands.PlatformListCommand;
import dev.letsgoaway.geyserextras.spigot.commands.TabListCommand;
import dev.letsgoaway.geyserextras.spigot.parity.bedrock.EmoteUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class GeyserExtras extends JavaPlugin implements PluginMessageListener {
    public static GeyserExtras plugin;
    public static Logger logger;
    public static BedrockAPI bedrockAPI;

    public GeyserExtras() {
        ServerType.type = ServerType.SPIGOT;
    }

    @Override
    public void onEnable() {
        plugin = this;
        logger = this.getLogger();
        EmoteUtils.load();
        bedrockAPI = new BedrockAPI();
        Instant start = Instant.now();
        logger.info("--------------GeyserExtras--------------");
        logger.info("Version: " + VersionConstants.PLUGIN_VERSION);
        logger.info("Server Type: " + ServerType.get());
        if (bedrockAPI.foundGeyserClasses) {
            StringBuilder types = new StringBuilder();
            for (APIType type : bedrockAPI.apiInstances.keySet()) {
                types.append(type.toString() + ", ");
            }
            logger.info("API Types: " + types.substring(0, types.length() - 2));
        } else {
            logger.info("GeyserExtras could not initialize! This means that Floodgate or Geyser was not in your plugins folder.");
            logger.info("----------------------------------------");
            this.setEnabled(false);
            return;
        }
        Objects.requireNonNull(this.getCommand("geyserextras")).setExecutor(new GeyserExtrasCommand());
        Objects.requireNonNull(this.getCommand("platformlist")).setExecutor(new PlatformListCommand());
        Objects.requireNonNull(this.getCommand("playerlist")).setExecutor(new TabListCommand());
        Objects.requireNonNull(this.getCommand("emotechat")).setExecutor(new EmoteChatCommand());
        logger.info("Loading config...");
        Config.loadConfig();
        logger.info("Config loaded!");
        loadGeyserOptionalPack();
        if (!getDataFolder().toPath().resolve("GeyserExtrasPack.mcpack").toFile().exists()) {
            plugin.saveResource("GeyserExtrasPack.mcpack", false);
        }
        bedrockAPI.onLoadConfig();
        logger.info("Registering events...");
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        logger.info("Events registered!");
        if (Config.proxyMode) {
            logger.info("Registering proxy channels...");
            getServer().getMessenger().registerIncomingPluginChannel(this, "geyserextras:emote", this);
            getServer().getMessenger().registerOutgoingPluginChannel(this, "geyserextras:fog");
            logger.info("Proxy channels registered!");
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::update, 0L, 0L);
        DecimalFormat r3 = new DecimalFormat("0.000");
        Instant finish = Instant.now();
        logger.info("Done! (" + r3.format(Duration.between(start, finish).toMillis() / 1000.0d) + "s)");
        logger.info("----------------------------------------");
    }

    public void loadGeyserOptionalPack() {

        if (!getDataFolder().toPath().resolve("GeyserOptionalPack.mcpack").toFile().exists()) {
            logger.info("Downloading GeyserOptionalPack...");
            InputStream in = null;
            try {
                in = new URL("https://download.geysermc.org/v2/projects/geyseroptionalpack/versions/latest/builds/latest/downloads/geyseroptionalpack").openStream();
                Files.copy(in, getDataFolder().toPath().resolve("GeyserOptionalPack.mcpack"), StandardCopyOption.REPLACE_EXISTING);
                logger.info("GeyserOptionalPack downloaded!");
            } catch (IOException e) {
            }
        }
    }

    public static ConcurrentHashMap<UUID, BedrockPlayer> bplayers = new ConcurrentHashMap<>();

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (bedrockAPI.isBedrockPlayer(player.getUniqueId()) && !bplayers.containsKey(player.getUniqueId())) {
                bplayers.putIfAbsent(player.getUniqueId(), new BedrockPlayer(player));
            }
        }
        bplayers.keySet().forEach((uuid) -> {
            if (!Bukkit.getOfflinePlayer(uuid).isOnline()) {
                bplayers.remove(uuid);
            }
        });
        for (BedrockPlayer bplayer : bplayers.values()) {
            bplayer.update();
        }
    }

    @Override
    public void onDisable() {
        for (BedrockPlayer bplayer : bplayers.values()) {
            bplayer.save();
            if (Config.autoReconnect) {
                bedrockAPI.reconnect(bplayer.player.getUniqueId());
            }
        }
    }

    @Override
    public void onPluginMessageReceived(@Nonnull String channel, @Nonnull Player player, @Nonnull byte[] message) {
        if (Config.proxyMode) {
            if (channel.equals("geyserextras:emote")) {
                bplayers.get(player.getUniqueId()).onPlayerEmoteEvent(new String(message, StandardCharsets.UTF_8));
            }
        }
    }
}
