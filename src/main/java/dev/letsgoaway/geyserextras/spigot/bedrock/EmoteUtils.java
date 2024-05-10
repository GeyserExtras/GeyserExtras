package dev.letsgoaway.geyserextras.spigot.bedrock;

import com.google.gson.Gson;
import dev.letsgoaway.geyserextras.spigot.GeyserExtras;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EmoteUtils {
    public static List<Emote> emotes;

    public static void load() {
        try {
            emotes = Arrays.asList(new Gson().fromJson(new String(Objects.requireNonNull(GeyserExtras.plugin.getResource("emotes.json")).readAllBytes(), StandardCharsets.UTF_8), Emote[].class));
        } catch (Exception ignored) {
        }
    }

    public static String getEmoteName(String uuid) {
        if (uuid.isEmpty() || uuid.isBlank()){
            return "Not Set";
        }
        for (Emote emote : emotes) {
            if (emote.uuid.equals(uuid)) {
                return emote.name;
            }
        }
        return "Unknown (" + uuid + ")";
    }

    public static String getEmoteChatString(String uuid) {
        if (uuid.isEmpty() || uuid.isBlank()){
            return "Not Set";
        }
        for (Emote emote : emotes) {
            if (emote.uuid.equals(uuid)) {
                return emote.message;
            }
            else {
                return emote.specialmessage;
            }
        }
        return "";
    }
}
