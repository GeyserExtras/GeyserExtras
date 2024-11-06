package dev.letsgoaway.geyserextras.core.cache;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class Cache {
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static Path CACHE_FOLDER;
    public static CacheDates CACHE_DATES;

    public static Path DATES_PATH;
    public static Path CREDITS_PATH;

    public static Path LANGUAGE_FOLDER;

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

        PackCacheUtils.initialize();
        SERVER.log("Loading resources...");

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
            downloadLanguages();
        }
    }

    public static String CREDITS_TEXT = "";

    public static void loadData() {
        try (FileInputStream data = new FileInputStream(CREDITS_PATH.toFile())) {
            CREDITS_TEXT = new String(data.readAllBytes()).replaceAll("&", "§");
        } catch (Exception e) {
        }
    }


    private static void downloadCredits() {
        try {
            InputStream in = new URL("https://raw.githubusercontent.com/GeyserExtras/data/main/credits.txt").openStream();
            Files.copy(in, CREDITS_PATH, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
        }
    }

    private static void downloadLanguages() {
        try {
            byte[] data = new URL("https://raw.githubusercontent.com/GeyserExtras/data/main/langs/language_names.json").openStream().readAllBytes();
            JsonNode languageNamesJson = JSON_MAPPER.readTree(data);
        } catch (IOException ignored) {
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    // Check if we need to update data here.
    private static boolean checkData() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            byte[] data = new URL("https://api.github.com/repos/GeyserExtras/data/branches/main").openStream().readAllBytes();
            JsonNode clientDataJson = JSON_MAPPER.readTree(data);
            long githubDataTime = dateFormat.parse(clientDataJson.get("commit").get("commit").get("author").get("date").asText()).toInstant().getEpochSecond();

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
}
