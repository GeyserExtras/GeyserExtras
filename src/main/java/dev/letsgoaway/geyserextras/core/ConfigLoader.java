package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.config.GeyserExtrasConfig;
import dev.letsgoaway.geyserextras.extension.GeyserExtrasExtension;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;
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

public class ConfigLoader {

    public static GeyserExtrasConfig config;

    private static final ConfigurationTransformation.Versioned transformer = ConfigurationTransformation.versionedBuilder()
            .addVersion(1, zeroToOne())
            .build();

    public static void load() {
        Path configPath = SERVER.getPluginFolder().resolve("config.yml");

        if (ServerType.type.equals(ServerType.EXTENSION)) {
            GeyserExtrasExtension extension = GeyserExtrasExtension.EXTENSION;
            // Ensure the data folder exists
            if (!extension.dataFolder().toFile().exists()) {
                if (!extension.dataFolder().toFile().mkdirs()) {
                    extension.logger().error("Failed to create data folder");
                }
            }
        }

        boolean existingConfig = configPath.toFile().exists();

        // Load the config, or create if needed
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .indent(2)
                .build();

        try {
            final CommentedConfigurationNode configurationNode = loader.load();
            config = configurationNode.get(GeyserExtrasConfig.class);

            var versionNode = configurationNode.node("version");
            if (versionNode.virtual() && existingConfig) {
                transformer.apply(configurationNode);
            }

            // Loads configs initially, or the transformed versions
            loader.save(configurationNode);
        } catch (ConfigurateException e) {
            // todo logging?
            throw new IllegalStateException("Failed to load config", e);
        }

        try {
            if (config.isEnableJavaOnlyBlockPlacement()) {
                updateGeyserConfig();
            }
        } catch (ConfigurateException e) {
            // TODO logging
            throw new IllegalStateException("Failed to change Geyser config!", e);
        }
    }

    private static ConfigurationTransformation zeroToOne() {
        return ConfigurationTransformation.builder()
                .addAction(NodePath.path("proxy-mode"), TransformAction.remove())
                .build();
    }

    public static void updateGeyserConfig() throws ConfigurateException {
        Path configPath = GE.geyserApi.configDirectory().resolve("config.yml");
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(configPath.toFile()).build();
        CommentedConfigurationNode geyserConfig = loader.load();
        if (config.isEnableJavaOnlyBlockPlacement()) {
            geyserConfig.node("disable-bedrock-scaffolding").set(true);
        }
        loader.save(geyserConfig);
    }
}
