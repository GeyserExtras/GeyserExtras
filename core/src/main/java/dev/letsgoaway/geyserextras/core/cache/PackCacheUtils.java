package dev.letsgoaway.geyserextras.core.cache;

import com.fasterxml.jackson.databind.JsonNode;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.parity.java.menus.packs.PackLoader;
import dev.letsgoaway.geyserextras.core.version.Version3;
import org.geysermc.api.Geyser;
import org.geysermc.geyser.api.event.bedrock.SessionLoadResourcePacksEvent;
import org.geysermc.geyser.api.pack.PackCodec;
import org.geysermc.geyser.api.pack.ResourcePack;
import org.geysermc.geyser.api.pack.option.PriorityOption;
import org.geysermc.geyser.api.pack.option.SubpackOption;

import org.geysermc.geyser.api.pack.UrlPackCodec;

import java.io.File;
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
    public static Path GEYSER_EXTRAS_PACK;
    public static ResourcePack RP_GEYSER_EXTRAS;

    public static void initialize() {
        PACKS_FOLDER = Cache.CACHE_FOLDER.resolve("packs/");
        GEYSER_EXTRAS_PACK = PACKS_FOLDER.resolve("GeyserExtrasPack.mcpack");
        SERVER.log("Checking for pack updates...");
        try {
            Files.createDirectories(PACKS_FOLDER);
            boolean extrasPackNeedsUpdate = checkExtrasPack();
            if (extrasPackNeedsUpdate) {
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
        RP_GEYSER_EXTRAS = ResourcePack.create(PackCodec.path(GEYSER_EXTRAS_PACK));

        // temporary, remove when geyserextras is released as its to remove a workaround for a bug i fixed that i suggested in the discord
        Path geyserMCPacks = GE.geyserApi.packDirectory();
        File geWorkaroundPack = geyserMCPacks.resolve("GeyserExtrasPack.mcpack").toFile();
        if (geWorkaroundPack.exists()) {
            if (geWorkaroundPack.delete()) {
                SERVER.log("Deleted GeyserExtrasPack.mcpack from GeyserMC packs folder");
            }
        }
        // delete old GeyserOptionalPack if it exists (deprecated, now included in Geyser by default)
        File gopWorkaroundPack = geyserMCPacks.resolve("GeyserOptionalPack.mcpack").toFile();
        if (gopWorkaroundPack.exists()) {
            if (gopWorkaroundPack.delete()) {
                SERVER.log("Deleted deprecated GeyserOptionalPack.mcpack from GeyserMC packs folder");
            }
        }
        // delete old GeyserOptionalPack from cache folder if it exists
        File oldOptionalPack = PACKS_FOLDER.resolve("GeyserOptionalPack.mcpack").toFile();
        if (oldOptionalPack.exists()) {
            if (oldOptionalPack.delete()) {
                SERVER.log("Deleted deprecated GeyserOptionalPack.mcpack from cache folder");
            }
        }
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
            UUID extrasPackUuid = RP_GEYSER_EXTRAS.manifest().header().uuid();

            // check if GeyserExtrasPack is already registered (e.g. via CDN/UrlPackCodec)
            ResourcePack existingPack = null;
            for (ResourcePack pack : ev.resourcePacks()) {
                if (pack.manifest().header().uuid().equals(extrasPackUuid)) {
                    existingPack = pack;
                    break;
                }
            }

            if (existingPack != null) {
                // pack already registered - check if it's via CDN
                if (existingPack.codec() instanceof UrlPackCodec) {
                    SERVER.log("GeyserExtrasPack detected via CDN (UrlPackCodec), skipping local registration.");
                } else {
                    SERVER.log("GeyserExtrasPack already registered, skipping.");
                }
            } else {
                // register local copy of the GeyserExtrasPack
                int priority = GE.getConfig().getGeyserExtrasPackPriority();
                // Geyser requires priority to be between -100 and 100
                if (priority < -100 || priority > 100) {
                    SERVER.warn("GeyserExtrasPack priority " + priority + " is out of valid range (-100 to 100). Using default value of 100.");
                    priority = 100;
                }
                ev.register(RP_GEYSER_EXTRAS, PriorityOption.priority(priority));
            }
        } catch (Exception ignored) {
        }
        List<UUID> packsToLoad = player.getPreferences().getSelectedPacks();
        for (UUID pack : packsToLoad) {
            ResourcePack rp = PackLoader.PACKS.get(pack);
            try {
                if (ev.resourcePacks().contains(rp)) {
                    continue;
                }
                String subpack = "";
                if (player.getPreferences().getSelectedSubpacks().containsKey(pack)) {
                    subpack = player.getPreferences().getSelectedSubpacks().get(pack);
                }

                ev.register(rp, PriorityOption.priority(99 - packsToLoad.indexOf(pack)), SubpackOption.named(subpack));
            } catch (Exception ignored) {
            }
        }
    }
}
