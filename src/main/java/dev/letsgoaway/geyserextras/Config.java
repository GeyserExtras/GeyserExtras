package dev.letsgoaway.geyserextras;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static boolean customCoolDownEnabled = true;
    public static boolean javaBlockPlacement = false;
    public static boolean blockGhostingFix = false;
    public static boolean autoReconnect = true;
    public static String externalAddress = "localhost";
    public static int externalPort = 19132;

    public static ArrayList<String> configQuickMenuActions = new ArrayList<>(Arrays.asList(
            "Swap Items with Off-hand >> /geyser offhand", "Toggle Tooltips >> /geyser tooltips",
            "Open GeyserExtras Menu >> /ge", "Open Advancements >> /geyser advancements", "Open Statistics >> /geyser statistics"));
    public static Map<String, String> quickMenuCommands = new HashMap<>();

    public static boolean knockbackAttackSprintFix = true;

    public static boolean proxyMode = false;

    public static boolean skinSavingEnabled = false;
    public static boolean enableArmsOnArmorStands = false;

    public static FileConfiguration config;

    public static FileConfiguration geyserConfig;

    public static FileConfiguration floodgateConfig;


    public static boolean netherRoofEnabled;
    public static Path skinsFolder;

    private static void loadValues() {
        if (config.contains("enable-custom-cooldown", true)) {
            customCoolDownEnabled = config.getBoolean("enable-custom-cooldown");
        }
        if (config.contains("enable-java-only-block-placement", true)) {
            javaBlockPlacement = config.getBoolean("enable-java-only-block-placement");
        }
        if (config.contains("enable-block-ghosting-fix", true)) {
            blockGhostingFix = config.getBoolean("enable-block-ghosting-fix");
        }
        if (config.contains("auto-reconnect", true)) {
            autoReconnect = config.getBoolean("auto-reconnect");
        }
        if (config.contains("external-address", true)) {
            externalAddress = config.getString("external-address");
        }
        if (config.contains("external-port", true)) {
            externalPort = config.getInt("external-port");
        }
        if (config.contains("quick-menu-actions")) {
            configQuickMenuActions = new ArrayList<>(config.getStringList("quick-menu-actions"));
            quickMenuCommands.clear();
            configQuickMenuActions.forEach((action) -> {
                String name = action.split(" >> ")[0];
                String command = action.split(" >> ")[1].substring(1);
                quickMenuCommands.put(name, command);
            });
            quickMenuCommands.put("None", "");
        }
        if (config.contains("enable-knockback-attack-sprint-fix", true)) {
            knockbackAttackSprintFix = config.getBoolean("enable-knockback-attack-sprint-fix");
        }
        if (config.contains("enable-skin-saving", true)) {
            skinSavingEnabled = config.getBoolean("enable-skin-saving");
        }
        if (config.contains("proxy-mode", true)) {
            proxyMode = config.getBoolean("proxy-mode");
        }
    }

    private static void saveValues() {
        config.set("enable-custom-cooldown", customCoolDownEnabled);
        config.set("enable-java-only-block-placement", javaBlockPlacement);
        config.set("enable-block-ghosting-fix", blockGhostingFix);
        config.set("auto-reconnect", autoReconnect);
        config.set("external-address", externalAddress);
        config.set("external-port", externalPort);
        config.set("quick-menu-actions", configQuickMenuActions);
        config.set("enable-knockback-attack-sprint-fix", knockbackAttackSprintFix);
        config.set("enable-skin-saving", skinSavingEnabled);
        config.set("proxy-mode", proxyMode);
        try {
            config.save(GeyserExtras.plugin.getDataFolder().toPath().resolve("config.yml").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path optionalPacks;
    public static File[] packsArray;

    // this config code is bad and i hate it
    public static void loadConfig() {
        if (!GeyserExtras.plugin.getDataFolder().exists()) {
            GeyserExtras.plugin.saveResource("config.yml", false);
        }
        config = GeyserExtras.plugin.getConfig();
        loadValues();
        GeyserExtras.plugin.saveResource("config.yml", true); // save config
        config = GeyserExtras.plugin.getConfig();
        GeyserExtras.plugin.reloadConfig();
        // and save the values again so that if any new values are added in an update it will be default but other settings are user preference
        saveValues();
        GeyserExtras.plugin.reloadConfig();
        try {
            optionalPacks = Files.createDirectories(Paths.get(GeyserExtras.plugin.getDataFolder().toURI().resolve("optionalpacks/")));
            packsArray = optionalPacks.toFile().listFiles();
            skinsFolder = Files.createDirectories(Paths.get(GeyserExtras.plugin.getDataFolder().toURI().resolve("skins/")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Plugin geyserSpigot = Bukkit.getPluginManager().getPlugin("Geyser-Spigot");
        assert geyserSpigot != null;
        File geyserDataFolder = null;
        if (geyserSpigot != null) {
            geyserDataFolder = geyserSpigot.getDataFolder();
            geyserConfig = geyserSpigot.getConfig();
        }
        Plugin floodgate = Bukkit.getPluginManager().getPlugin("floodgate");
        if (floodgate != null) {
            floodgateConfig = floodgate.getConfig();
        }
        if (geyserConfig != null) {
            Config.netherRoofEnabled = geyserConfig.getBoolean("above-bedrock-nether-building");
        } else {
            Config.netherRoofEnabled = Config.proxyMode;
        }
        if (Config.netherRoofEnabled) {
            GeyserExtras.logger.info("Nether Roof Fix Fog Fix enabled!");
        }
        if (geyserConfig == null) {
            GeyserExtras.logger.warning("Make sure 'force-resource-packs: true' in Geyser's Config!");
            if (Config.customCoolDownEnabled) {
                GeyserExtras.logger.warning("Set 'show-cooldown: false' in Geyser's Config for the cooldown!");
            }
        } else {
            geyserConfig.set("show-cooldown", "false");
            if (geyserDataFolder != null) {
                Path packLocation = geyserDataFolder.toPath().resolve("packs/GeyserExtrasPack.mcpack");
                if (packLocation.toFile().exists()) {
                    packLocation.toFile().delete();
                }
                GeyserExtras.plugin.saveResource("GeyserExtrasPack.mcpack", false);
            }
        }
        if (geyserDataFolder != null && geyserConfig != null) {
            try {
                geyserConfig.save(geyserDataFolder.toPath().resolve("config.yml").toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

