package dev.letsgoaway.geyserextras.core.cache;

import com.fasterxml.jackson.databind.JsonNode;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.parity.java.menus.packs.PackLoader;
import dev.letsgoaway.geyserextras.core.version.Version3;
import org.geysermc.geyser.api.event.bedrock.SessionLoadResourcePacksEvent;
import org.geysermc.geyser.api.pack.PackCodec;
import org.geysermc.geyser.api.pack.ResourcePack;
import org.geysermc.geyser.api.pack.option.PriorityOption;
import org.geysermc.geyser.pack.option.GeyserPriorityOption;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;
import static dev.letsgoaway.geyserextras.core.cache.Cache.CACHE_DATES;
import static dev.letsgoaway.geyserextras.core.cache.Cache.JSON_MAPPER;

public class PackCacheUtils {
    public static Path PACKS_FOLDER;
    public static Path GEYSER_OPTIONAL_PACK;
    public static Path GEYSER_EXTRAS_PACK;
    public static ResourcePack RP_GEYSER_OPTIONAL;
    public static ResourcePack RP_GEYSER_EXTRAS;

    public static void initialize() {
        PACKS_FOLDER = Cache.CACHE_FOLDER.resolve("packs/");
        GEYSER_OPTIONAL_PACK = PACKS_FOLDER.resolve("GeyserOptionalPack.mcpack");
        GEYSER_EXTRAS_PACK = PACKS_FOLDER.resolve("GeyserExtrasPack.mcpack");
        SERVER.log("Checking for pack updates...");
        try {
            Files.createDirectories(PACKS_FOLDER);
            boolean optionalPackNeedsUpdate = checkOptionalPack();
            boolean extrasPackNeedsUpdate = checkExtrasPack();
            if (optionalPackNeedsUpdate || extrasPackNeedsUpdate) {
                Cache.saveCacheDates();
            }
            if (extrasPackNeedsUpdate) {
                SERVER.log("Downloading GeyserExtrasPack...");
                InputStream in = HTTP.request("https://raw.githubusercontent.com/GeyserExtras/GeyserExtrasPack/main/GeyserExtrasPack.mcpack");
                Files.copy(in, GEYSER_EXTRAS_PACK, StandardCopyOption.REPLACE_EXISTING);
                SERVER.log("GeyserExtrasPack downloaded!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading resources!", e);
        }
        SERVER.log("Loading resources...");
        RP_GEYSER_OPTIONAL = ResourcePack.create(PackCodec.path(GEYSER_OPTIONAL_PACK));
        RP_GEYSER_EXTRAS = ResourcePack.create(PackCodec.path(GEYSER_EXTRAS_PACK));
    }

    private static boolean checkOptionalPack() {
        // Download the pack if it exists
        if (!GEYSER_OPTIONAL_PACK.toFile().exists()) {
            return true;
        }

        // Otherwise check for updates
        if (!GE.getConfig().isCheckForUpdates()) {
            return false;
        }
        Version3 oldVersion = Version3.fromArray(CACHE_DATES.lastOptionalPackVersion);
        Version3 latestOptionalPackVersion = getPackVersion("https://raw.githubusercontent.com/GeyserMC/GeyserOptionalPack/master/manifest.json");
        CACHE_DATES.lastOptionalPackVersion = latestOptionalPackVersion.asArray();
        return latestOptionalPackVersion.isNewer(oldVersion);
    }

    private static boolean checkExtrasPack() {
        // Download the pack if it exists
        if (!GEYSER_EXTRAS_PACK.toFile().exists()) {
            return true;
        }

        // Otherwise check for updates
        if (!GE.getConfig().isCheckForUpdates()) {
            return false;
        }
        Version3 oldVersion = Version3.fromArray(CACHE_DATES.lastExtrasPackVersion);
        Version3 latestExtrasPackVersion = getPackVersion("https://raw.githubusercontent.com/GeyserExtras/GeyserExtrasPack/main/src/pack/manifest.json");
        CACHE_DATES.lastExtrasPackVersion = latestExtrasPackVersion.asArray();
        return latestExtrasPackVersion.isNewer(oldVersion);
    }

    private static Version3 getPackVersion(String url) {
        try {
            byte[] data = new URL(url).openStream().readAllBytes();
            JsonNode manifestJson = JSON_MAPPER.readTree(data);
            ArrayList<Integer> versionArray = new ArrayList<>();
            for (JsonNode node : manifestJson.get("header").get("version")) {
                versionArray.add(node.asInt());
            }
            return Version3.fromList(versionArray);
        } catch (Exception e) {
            return Version3.fromString("0.0.0");
        }
    }

    public static void onPackLoadEvent(ExtrasPlayer player, SessionLoadResourcePacksEvent ev) {
        try {
            if (!ev.resourcePacks().contains(RP_GEYSER_OPTIONAL)) {
                ev.register(RP_GEYSER_OPTIONAL, GeyserPriorityOption.HIGHEST);
            }
        } catch (Exception e) {
        }
        try {
            if (!ev.resourcePacks().contains(RP_GEYSER_EXTRAS)) {
                ev.register(RP_GEYSER_EXTRAS, GeyserPriorityOption.HIGHEST);
            }
        } catch (Exception e) {
        }
        List<UUID> packsToLoad = player.getPreferences().getSelectedPacks();
        for (UUID pack : packsToLoad) {
            ResourcePack rp = PackLoader.PACKS.get(pack);
            try {
                if (ev.resourcePacks().contains(rp)) {
                    continue;
                }
                ev.register(rp, PriorityOption.priority(packsToLoad.indexOf(pack) - 99));
            } catch (Exception e) {
            }
        }
    }
}
