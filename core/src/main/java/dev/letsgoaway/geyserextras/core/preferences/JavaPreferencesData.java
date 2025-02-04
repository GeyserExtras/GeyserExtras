package dev.letsgoaway.geyserextras.core.preferences;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;
import static dev.letsgoaway.geyserextras.core.cache.Cache.JSON_MAPPER;

public class JavaPreferencesData {
    @JsonIgnore
    public static Path JAVA_PREFRENCES_PATH;
    @JsonIgnore
    public static JavaPreferencesData DEFAULT;
    @JsonIgnore
    public UUID javaUUID;
    @JsonIgnore
    public File saveFile;
    public boolean muteEmoteChat = false;


    public JavaPreferencesData() {
        this.javaUUID = null;
    }

    public JavaPreferencesData(UUID javaUUID) {
        this.javaUUID = javaUUID;
        this.saveFile = JAVA_PREFRENCES_PATH.resolve(javaUUID.toString() + ".json").toFile();
        if (saveFile.exists()) {
            try {
                FileInputStream data = new FileInputStream(saveFile);

                this.copyFrom(JSON_MAPPER.convertValue(JSON_MAPPER.readTree(data.readAllBytes()), JavaPreferencesData.class));
            } catch (Exception e) {
                SERVER.warn("Could not load data for player " + javaUUID + ", restoring to default for them.\n" + e.getLocalizedMessage());

                this.copyFrom(DEFAULT);
            }
        } else {
            this.copyFrom(DEFAULT);
        }
    }

    public static void init() {
        JAVA_PREFRENCES_PATH = PreferencesData.PREFERENCES_PATH.resolve("java/");
        try {
            Files.createDirectories(JAVA_PREFRENCES_PATH);
        } catch (Exception ignored) {
        }

        Path defaultPath = JAVA_PREFRENCES_PATH.resolve("default.json");
        DEFAULT = new JavaPreferencesData();
        try {
            if (defaultPath.toFile().exists()) {
                FileInputStream data = new FileInputStream(defaultPath.toFile());
                // Copy from because defaults might be different
                DEFAULT.copyFrom(JSON_MAPPER.convertValue(JSON_MAPPER.readTree(data.readAllBytes()), JavaPreferencesData.class));
            }
            JSON_MAPPER.writeValue(defaultPath.toFile(), DEFAULT);
        } catch (Exception e) {
            SERVER.warn("Could not load custom default Java player settings, new players will recieve factory\ndefault's, no changes have been made to default.json.\n" + e.getLocalizedMessage());
        }
    }

    public static JavaPreferencesData load(UUID javaUUID) {
        return new JavaPreferencesData(javaUUID);
    }

    public void copyFrom(JavaPreferencesData data) {
        this.muteEmoteChat = data.muteEmoteChat;
    }

    public void save() {
        new Thread(() -> {
            try {
                if (!JSON_MAPPER.writeValueAsString(this).equals(JSON_MAPPER.writeValueAsString(DEFAULT))) {
                    JSON_MAPPER.writeValue(saveFile, this);
                }
            } catch (IOException e) {
                SERVER.warn("Could not save data for player " + javaUUID + "\n" + e.getLocalizedMessage());
            }
        }).start();
    }
}
