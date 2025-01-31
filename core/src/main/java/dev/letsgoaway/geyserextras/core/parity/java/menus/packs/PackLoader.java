package dev.letsgoaway.geyserextras.core.parity.java.menus.packs;

import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.pack.PackCodec;
import org.geysermc.geyser.api.pack.ResourcePack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class PackLoader {
    public static Path PACKS_PATH;

    public static Map<UUID, ResourcePack> PACKS = new LinkedHashMap<>();

    public static void initialize() {
        PACKS_PATH = SERVER.getPluginFolder().resolve("optionalpacks/");
        try {
            Files.createDirectories(PACKS_PATH);
        } catch (IOException ignored) {
        }
        File packFolder = PACKS_PATH.toFile();
        File[] packs = packFolder.listFiles();

        if (packFolder.exists() && packs != null) {
            for (File pack : packs) {
                String ext = getFileExtension(pack);
                if (ext.equals(".mcpack")) {
                    ResourcePack mcpack = ResourcePack.create(PackCodec.path(pack.toPath()));
                    SERVER.log(mcpack.manifest().header().name() + " loaded!");
                    PACKS.put(mcpack.manifest().header().uuid(), mcpack);
                }
            }
        }
    }

    // https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
