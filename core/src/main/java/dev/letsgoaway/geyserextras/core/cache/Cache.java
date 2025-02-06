package dev.letsgoaway.geyserextras.core.cache;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.letsgoaway.geyserextras.core.locale.GELocale;
import dev.letsgoaway.geyserextras.core.parity.java.menus.packs.PackLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class Cache {
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    public static final Gson GSON = new Gson();
    public static Path CACHE_FOLDER;
    public static CacheDates CACHE_DATES;

    public static Path DATES_PATH;
    public static Path CREDITS_PATH;

    public static Path LANGUAGE_FOLDER;

    private static final String REPO = "https://raw.githubusercontent.com/GeyserExtras/data/main/";

    public static void initialize() {
        CACHE_FOLDER = SERVER.getPluginFolder().resolve("cache/");

        DATES_PATH = CACHE_FOLDER.resolve("dates.json");
        CREDITS_PATH = CACHE_FOLDER.resolve("credits.txt");
        LANGUAGE_FOLDER = CACHE_FOLDER.resolve("langs/");

        try {
            Files.createDirectories(CACHE_FOLDER);
            Files.createDirectories(LANGUAGE_FOLDER);
            if (Files.exists(DATES_PATH)) {
                FileInputStream data = new FileInputStream(DATES_PATH.toFile());
                CACHE_DATES = JSON_MAPPER.convertValue(JSON_MAPPER.readTree(data.readAllBytes()), CacheDates.class);
            } else {
                CACHE_DATES = new CacheDates();
                saveCacheDates();
            }
        } catch (Exception ignored) {
        }
        downloadAll();

        SERVER.log("Loading cache...");
        loadData();

        SERVER.log("Loading resources...");
        PackCacheUtils.initialize();
        PackLoader.initialize();
    }

    public static void saveCacheDates() {
        try {
            JSON_MAPPER.writeValue(DATES_PATH.toFile(), CACHE_DATES);
        } catch (IOException e) {
        }
    }

    public static void downloadAll() {
        boolean dataNeedsUpdate = checkData();
        if (dataNeedsUpdate || !CREDITS_PATH.toFile().exists()) {
            downloadCredits();
        }
        if (dataNeedsUpdate) {
            SERVER.log("Downloading languages...");
            downloadLanguages();
        }
    }

    public static String CREDITS_TEXT = "";

    public static void loadData() {
        try (FileInputStream data = new FileInputStream(CREDITS_PATH.toFile())) {
            CREDITS_TEXT = new String(data.readAllBytes()).replaceAll("&", "§");
        } catch (Exception e) {
        }
        GELocale.loadAll();
    }


    private static void downloadCredits() {
        InputStream in = HTTP.request(REPO+"credits.txt");
        try {
            Files.copy(in, CREDITS_PATH, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void downloadLanguages() {
        String langNames = HTTP.asText(REPO+"langs/language_names.json");

        try {
            PrintWriter out = new PrintWriter(LANGUAGE_FOLDER.resolve("language_names.json").toFile());
            out.println(langNames);
            out.close();

            String[][] langs = GSON.fromJson(langNames, String[][].class);

            for (String[] lang : langs) {
                InputStream langJson = HTTP.request(REPO+"langs/" + lang[0] + ".json");
                Files.copy(langJson, LANGUAGE_FOLDER.resolve(lang[0] + ".json"), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ignored) {
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    // Check if we need to update data here.
    private static boolean checkData() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            if (isDirEmpty(LANGUAGE_FOLDER)) {
                return true;
            }

            JsonObject clientDataJson = GSON.fromJson(HTTP.asText("https://api.github.com/repos/GeyserExtras/data/branches/main"), JsonObject.class);
            long githubDataTime = dateFormat.parse(clientDataJson
                    .get("commit").getAsJsonObject()
                    .get("commit").getAsJsonObject()
                    .get("author").getAsJsonObject()
                    .get("date").getAsString()
            ).toInstant().getEpochSecond();

            if (githubDataTime > CACHE_DATES.getDataUpdateTime()) {
                CACHE_DATES.setDataUpdateTime(githubDataTime);
                JSON_MAPPER.writeValue(DATES_PATH.toFile(), CACHE_DATES);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // https://stackoverflow.com/questions/5930087/how-to-check-if-a-directory-is-empty-in-java
    private static boolean isDirEmpty(final Path directory) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }
}
