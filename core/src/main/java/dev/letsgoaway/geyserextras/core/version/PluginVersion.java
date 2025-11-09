package dev.letsgoaway.geyserextras.core.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.letsgoaway.geyserextras.ServerType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class PluginVersion {
    public static final String GE_VERSION = "2.0.0-BETA-10";

    public static String latestVersion = "";
    public static String latestVersionModrinthID = "";


    public static boolean checkForUpdate() {
        if (!GE.getConfig().isCheckForUpdates()) {
            return false;
        }

        try {
            URL url = new URL("https://api.modrinth.com/v2/project/geyserextras/version");
            URLConnection request = url.openConnection();
            request.setConnectTimeout(5000);
            request.setRequestProperty("User-Agent", "GeyserExtras/GeyserExtras/" + GE_VERSION);
            request.connect();
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));

            for (JsonElement version : root.getAsJsonArray()) {
                List<String> loadersArray = new ArrayList<>();
                JsonArray loaders = version.getAsJsonObject().get("loaders").getAsJsonArray();
                loaders.forEach(loader -> {
                    loadersArray.add(loader.getAsString());
                });
                if (loadersArray.contains(ServerType.type.name().toLowerCase()) || (ServerType.isExtension() && loadersArray.contains("geyser"))) {
                    latestVersion = version.getAsJsonObject().get("version_number").getAsString();
                    latestVersionModrinthID = version.getAsJsonObject().get("id").getAsString();
                    return !latestVersion.equals(GE_VERSION);
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static void checkForUpdatesAndPrintToLog() {
        if (!GE.getConfig().isCheckForUpdates()) {
            return;
        }

        new Thread(() -> {
            if (checkForUpdate()) {
                SERVER.warn("There is a new update to GeyserExtras!");
                SERVER.warn("You are on version " + GE_VERSION + " but the latest version is " + latestVersion + ".");
                SERVER.warn("Download & Changelog: https://modrinth.com/plugin/geyserextras/version/" + latestVersionModrinthID);
            } else {
                SERVER.warn("GeyserExtras is on the latest version.");
            }
        }).start();
    }
}
