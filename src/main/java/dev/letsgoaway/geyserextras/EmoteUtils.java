package dev.letsgoaway.geyserextras;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EmoteUtils {
    public static List<Emote> emotes;

    public static void load() {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            result.write(Objects.requireNonNull(GeyserExtras.plugin.getResource("emotes.json")).readAllBytes());
            emotes = Arrays.asList(new Gson().fromJson(result.toString(StandardCharsets.UTF_8), Emote[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
