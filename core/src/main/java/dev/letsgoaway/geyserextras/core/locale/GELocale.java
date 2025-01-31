package dev.letsgoaway.geyserextras.core.locale;

import com.google.gson.JsonObject;
import dev.letsgoaway.geyserextras.core.cache.Cache;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class GELocale {
    private static HashMap<String, HashMap<String, String>> LANGUAGES_MAP = new HashMap<>();

    public static void loadAll() {
        try {
            FileInputStream data = new FileInputStream(Cache.LANGUAGE_FOLDER.resolve("language_names.json").toFile());

            String langNames = new String(data.readAllBytes(), StandardCharsets.UTF_8);
            String[][] langs = Cache.GSON.fromJson(langNames, String[][].class);
            for (String[] lang : langs) {
                FileInputStream langData = new FileInputStream(Cache.LANGUAGE_FOLDER.resolve(lang[0] + ".json").toFile());
                JsonObject json = Cache.GSON.fromJson(new String(langData.readAllBytes(), StandardCharsets.UTF_8), JsonObject.class);
                HashMap<String, String> languageData = new HashMap<>();
                for (String key : json.keySet()) {
                    String translation = json.get(key).getAsString();

                    while (translation.contains("/{") && translation.contains("}/")) {
                        for (String key2 : json.keySet()) {
                            if (translation.contains("/{" + key2 + "}/")) {
                                translation = translation.replace("/{" + key2 + "}/", json.get(key2).getAsString());
                            }
                        }
                    }

                    languageData.put(key, translation);
                }
                LANGUAGES_MAP.put(lang[0], languageData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String translate(String key, String locale) {
        if (LANGUAGES_MAP.containsKey(locale)) {
            return LANGUAGES_MAP.get(locale).getOrDefault(key, key);
        }
        return LANGUAGES_MAP.get("en_US").getOrDefault(key, key);
    }
}
