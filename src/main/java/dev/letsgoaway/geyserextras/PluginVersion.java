package dev.letsgoaway.geyserextras;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class PluginVersion {
    public static final String GE_VERSION = "2.0.0-ALPHA-11";

    public static String latestVersion = "";

    public static boolean checkForUpdate() {
        try {
            URL url = new URL("https://api.modrinth.com/v2/project/geyserextras/version");
            URLConnection request = url.openConnection();
            request.setConnectTimeout(5000);
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject latest = root.getAsJsonArray().get(0).getAsJsonObject();
            latestVersion = latest.get("version_number").getAsString();
            return !latestVersion.equals(GE_VERSION);
        } catch (IOException e) {
            return false;
        }
    }

    public static void checkForUpdatesAndPrintToLog() {
        new Thread(() -> {
            if (checkForUpdate()) {
                SERVER.warn("There is a new update to GeyserExtras!");
                SERVER.warn("You are on version " + GE_VERSION + " but the latest version is " + latestVersion + ".");
                SERVER.warn("Download & Changelog: https://modrinth.com/plugin/geyserextras/version/" + latestVersion);
            }
            else {
                SERVER.warn("GeyserExtras is on the latest version.");
            }
        }).start();
    }
}
