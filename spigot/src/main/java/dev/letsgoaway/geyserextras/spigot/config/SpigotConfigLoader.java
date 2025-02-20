package dev.letsgoaway.geyserextras.spigot.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;
import static dev.letsgoaway.geyserextras.spigot.GeyserExtrasSpigot.SPIGOT;

public class SpigotConfigLoader {

    public static final int LATEST_VERSION = 1;
    private static final String HEADER = """
            GeyserExtras - Spigot Configuration
            """;
    private static final ConfigurationTransformation.Versioned transformer = ConfigurationTransformation.versionedBuilder()
            .addVersion(1, zeroToOne())
            .build();


    public static void load() {
        Path configPath = SERVER.getPluginFolder().resolve("spigot.yml");

        boolean existingConfig = configPath.toFile().exists();

        // Load the config, or create if needed
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .headerMode(HeaderMode.PRESERVE)
                .defaultOptions(options -> options.header(HEADER))
                .build();

        try {
            final CommentedConfigurationNode configurationNode = loader.load();
            int currentVersion = transformer.version(configurationNode);
            if (currentVersion != LATEST_VERSION) {
                transformer.apply(configurationNode);
            }

            // Get the configured values based on our GeyserExtrasConfig class
            GeyserExtrasSpigotConfig config = configurationNode.get(GeyserExtrasSpigotConfig.class);

            // Let's not save the configuration options back to the original node, but create a new node instead
            // and save them there. This will allow us to order new configuration options.
            CommentedConfigurationNode newNode = CommentedConfigurationNode.root(loader.defaultOptions());
            newNode.set(GeyserExtrasSpigotConfig.class, config);

            // Save only when the config changed, or didn't exist in the first place
            if (!existingConfig || currentVersion != LATEST_VERSION) {
                loader.save(newNode);
            }

            SPIGOT.setPlatformConfig(config);
        } catch (ConfigurateException e) {
            // todo logging?
            SERVER.warn("Failed to load spigot config! Using defaults.\n" + e.getMessage());
            SPIGOT.setPlatformConfig(new GeyserExtrasSpigotConfig());
        }
    }

    private static ConfigurationTransformation zeroToOne() {
        return ConfigurationTransformation.builder()
                .build();
    }
}
