package dev.letsgoaway.geyserextras.core.parity.bedrock;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class EmoteUtils {
    public static float EMOTE_DISTANCE = 100.0F;
    private static final Random emoteRandom = new Random();
    private static JsonObject emotes;

    public static void initialize() {
        InputStream stream = EmoteUtils.class.getClassLoader().getResourceAsStream("emotes.json");
        if (stream != null) {
            try {
                String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                emotes = JsonParser.parseString(json).getAsJsonObject();
            } catch (IOException e) {
                SERVER.warn("Could not load emotes!");
            }
        } else {
            SERVER.warn("Could not load emotes!");
        }
    }

    public static String getName(UUID uuid) {
        JsonElement obj = emotes.get(uuid.toString());
        if (obj == null) {
            return null;
        }
        return obj.getAsJsonObject().get("name").getAsString();
    }

    private static String getEmoteChatRaw(UUID uuid) {
        JsonElement obj = emotes.get(uuid.toString());
        if (obj == null)
            return null;

        if (emoteRandom.nextInt(0, 16) == 4) {
            return obj.getAsJsonObject().get("specialmessage").getAsString();
        }

        return obj.getAsJsonObject().get("message").getAsString();
    }

    public static String getEmoteChatString(UUID uuid, ExtrasPlayer player) {
        String raw = getEmoteChatRaw(uuid);
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        raw = raw.replace("@", player.getSession().javaUsername());
        int amountOfAmpersand = 0;
        int i = 0;
        char[] parsed = raw.toCharArray();
        for (char charRealSmooth : parsed) {
            if (charRealSmooth == '&' || charRealSmooth == '$') {
                amountOfAmpersand++;
                // one hop this time!
                if (amountOfAmpersand == 1) {
                    parsed[i] = '\uE001';
                    // two hops this time!
                } else if (amountOfAmpersand == 2) {
                    parsed[i] = '\uE002';
                }
            }
            i++;
        }
        raw = new String(parsed);
        raw = raw.replace("\uE001", "§r§a");
        raw = raw.replace("\uE002", "§r");
        return raw;
    }
}
