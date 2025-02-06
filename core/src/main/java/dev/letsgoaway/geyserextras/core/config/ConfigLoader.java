package dev.letsgoaway.geyserextras.core.config;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.parity.java.blockdisplay.BlockDisplayEntity;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.configuration.GeyserConfiguration;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class ConfigLoader {

    public static final int LATEST_VERSION = 2;
    private static final String HEADER = """
            GeyserExtras
            If Geyser is detected, all required config changes will be automatically applied.
            For more details, go to https://geyserextras.github.io/
            """;
    private static final ConfigurationTransformation.Versioned transformer = ConfigurationTransformation.versionedBuilder()
            .addVersion(1, zeroToOne())
            .addVersion(2, oneToTwo())
            .build();


    public static void load() {
        Path configPath = SERVER.getPluginFolder().resolve("config.yml");

        if (ServerType.isExtension()) {
            // Ensure the data folder exists
            if (!SERVER.getPluginFolder().toFile().exists()) {
                if (!SERVER.getPluginFolder().toFile().mkdirs()) {
                    SERVER.warn("Failed to create data folder");
                }
            }
        }

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
            GeyserExtrasConfig config = configurationNode.get(GeyserExtrasConfig.class);

            // Let's not save the configuration options back to the original node, but create a new node instead
            // and save them there. This will allow us to order new configuration options.
            CommentedConfigurationNode newNode = CommentedConfigurationNode.root(loader.defaultOptions());
            newNode.set(GeyserExtrasConfig.class, config);

            // Save only when the config changed, or didn't exist in the first place
            if (!existingConfig || currentVersion != LATEST_VERSION) {
                loader.save(newNode);
            }

            GE.setConfig(config);
        } catch (ConfigurateException e) {
            // todo logging?
            SERVER.warn("Failed to load config! Using defaults.\n" + e.getMessage());
            GE.setConfig(new GeyserExtrasConfig());
        }

        if (GE.getConfig().isEnableBlockDisplayWorkaround()) {
            BlockDisplayEntity.buildEntityDef();
        }
        try {
            if (GE.getConfig().isEnableCustomCooldown()) {
                updateGeyserConfig();
            }
        } catch (ConfigurateException e) {
            SERVER.warn("Could not update Geyser config!\n" + e.rawMessage());
        }
        if (IsAvailable.floodgate() && GE.getConfig().isEnableGeyserExtrasMenu()) {
            SERVER.warn("WARNING: Floodgate is installed, so GeyserExtras Settings will not");
            SERVER.warn("show up in the Game Settings menu due to how forms work on GeyserMC.");
            SERVER.warn("If you want a temporary work around to this, use Geyser-Standalone,");
            SERVER.warn("otherwise a notification toast will show up informing players that");
            SERVER.warn("they will have to double tap inventory.");
        }
    }

    private static ConfigurationTransformation zeroToOne() {
        return ConfigurationTransformation.builder()
                .addAction(NodePath.path("proxy-mode"), TransformAction.remove())
                .addAction(NodePath.path("external-address"), TransformAction.remove())
                .addAction(NodePath.path("external-port"), TransformAction.remove())
                .addAction(NodePath.path("mute-emote-chat"), TransformAction.remove())
                .addAction(NodePath.path("quick-menu-actions"), TransformAction.remove())
                .addAction(NodePath.path("enable-java-only-block-placement"), TransformAction.remove())
                .addAction(NodePath.path("enable-block-ghosting-fix"), TransformAction.remove())
                .addAction(NodePath.path("enable-knockback-attack-sprint-fix"), TransformAction.remove())
                .build();
    }

    private static ConfigurationTransformation oneToTwo() {
        return ConfigurationTransformation.builder()
                .addAction(NodePath.path("quick-menu-actions"), TransformAction.remove())
                .addAction(NodePath.path("enable-java-only-block-placement"), TransformAction.remove())
                .addAction(NodePath.path("enable-block-ghosting-fix"), TransformAction.remove())
                .addAction(NodePath.path("enable-knockback-attack-sprint-fix"), TransformAction.remove())
                .build();
    }

    public static void updateGeyserConfig() throws ConfigurateException {
        Path configPath = GE.geyserApi.configDirectory().resolve("config.yml");
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(configPath.toFile()).build();

        CommentedConfigurationNode geyserConfig = loader.load();
        geyserConfig.node("show-cooldown").set("title");
        loader.save(geyserConfig);
    }
}
