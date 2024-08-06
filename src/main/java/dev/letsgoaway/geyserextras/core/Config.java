package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.extension.GeyserExtrasExtension;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class Config {
    //region config.yml values
    public static boolean customCoolDownEnabled = true;
    public static boolean javaCombatSounds = true;
    public static boolean javaBlockPlacement = false;
    public static boolean blockGhostingFix = false;
    public static boolean autoReconnect = true;
    public static ArrayList<String> configQuickMenuActions = new ArrayList<>(Arrays.asList(
            "Swap Items with Off-hand >> /geyser offhand", "Toggle Tooltips >> /geyser tooltips",
            "Open GeyserExtras Menu >> /ge", "Open Advancements >> /geyser advancements", "Open Statistics >> /geyser statistics",
            "Player List >> /playerlist",
            "Platform List >> /platformlist"));
    public static boolean knockbackAttackSprintFix = true;
    public static boolean skinSavingEnabled = false;
    public static boolean muteEmoteChat = false;
    public static boolean proxyMode = false;
    public static boolean disablePaperDoll = false;

    //endregion
    public static void load() {
        Path configPath = SERVER.getPluginFolder().resolve("config.yml");
        if (ServerType.type.equals(ServerType.EXTENSION)) {
            File configFile = SERVER.getPluginFolder().resolve("config.yml").toFile();
            GeyserExtrasExtension extension = GeyserExtrasExtension.EXTENSION;
            // Ensure the data folder exists
            if (!extension.dataFolder().toFile().exists()) {
                if (!extension.dataFolder().toFile().mkdirs()) {
                    extension.logger().error("Failed to create data folder");
                }
            }

            // Create the config file if it doesn't exist
            if (!configFile.exists()) {
                try (FileWriter writer = new FileWriter(configFile)) {
                    try (FileSystem fileSystem = FileSystems.newFileSystem(new File(GeyserExtrasExtension.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath(), Collections.emptyMap())) {
                        try (InputStream input = Files.newInputStream(fileSystem.getPath("config.yml"))) {
                            byte[] bytes = new byte[input.available()];
                            input.read(bytes);
                            writer.write(new String(bytes).toCharArray());
                            writer.flush();
                        }
                    }
                } catch (IOException | URISyntaxException e) {
                    GeyserExtrasExtension.EXTENSION.logger().error("Failed to create config", e);
                }
            }
        } else {
            if (!configPath.toFile().exists()) {
                try {
                    Files.createDirectories(SERVER.getPluginFolder());
                    InputStream stream = Config.class.getResourceAsStream("/config.yml");
                    OutputStream outStream = new FileOutputStream(configPath.toFile());
                    outStream.write(stream.readAllBytes());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(configPath.toFile()).build();
        try {
            CommentedConfigurationNode data = loader.load();
            if (data.hasChild("enable-custom-cooldown"))
                customCoolDownEnabled = data.node("enable-custom-cooldown").getBoolean();
            if (data.hasChild("enable-java-combat-sounds"))
                javaCombatSounds = data.node("enable-java-combat-sounds").getBoolean();
            if (data.hasChild("enable-java-only-block-placement"))
                javaBlockPlacement = data.node("enable-java-only-block-placement").getBoolean();
            if (data.hasChild("enable-block-ghosting-fix"))
                blockGhostingFix = data.node("enable-block-ghosting-fix").getBoolean();
            if (data.hasChild("auto-reconnect"))
                autoReconnect = data.node("auto-reconnect").getBoolean();
            if (data.hasChild("quick-menu-actions")) {
                List<String> actions = data.node("quick-menu-actions").getList(String.class);
                if (actions != null) {
                    configQuickMenuActions = new ArrayList<>(actions);
                }
            }
            if (data.hasChild("enable-knockback-attack-sprint-fix"))
                knockbackAttackSprintFix = data.node("enable-knockback-attack-sprint-fix").getBoolean();
            if (data.hasChild("enable-skin-saving"))
                skinSavingEnabled = data.node("enable-skin-saving").getBoolean();
            if (data.hasChild("mute-emote-chat"))
                muteEmoteChat = data.node("mute-emote-chat").getBoolean();
            if (data.hasChild("disable-paper-doll"))
                disablePaperDoll = data.node("disable-paper-doll").getBoolean();
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateGeyserConfig() throws ConfigurateException {
        Path configPath = GE.geyserApi.configDirectory().resolve("config.yml");
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(configPath.toFile()).build();
        CommentedConfigurationNode geyserConfig = loader.load();
        if (Config.javaBlockPlacement) {
            geyserConfig.node("disable-bedrock-scaffolding").set(true);
        }
        loader.save(geyserConfig);
    }
}
