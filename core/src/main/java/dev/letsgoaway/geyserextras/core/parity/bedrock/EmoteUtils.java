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
    private static final Random emoteRandom = new Random();
    public static float EMOTE_DISTANCE = 100.0F;
    public static JsonObject emotes;

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

    private static String getString(UUID uuid, String string) {
        JsonElement elem = emotes.get(uuid.toString());
        if (elem == null) {
            return null;
        }

        JsonObject obj = elem.getAsJsonObject();
        if (obj.get(string) == null) {
            return null;
        }

        return obj.get(string).getAsString();
    }

    public static String getThumbnail(UUID uuid) {
        return getString(uuid, "thumbnail");
    }

    public static String getCreator(UUID uuid) {
        return getString(uuid, "creator");
    }

    public static String getPrimary(UUID uuid) {
        return getString(uuid, "primary");
    }

    public static int getPrice(UUID uuid) {
        JsonElement elem = emotes.get(uuid.toString());
        if (elem == null) {
            return -1;
        }

        JsonObject obj = elem.getAsJsonObject();
        if (obj.get("price") == null) {
            return -1;
        }

        return obj.get("price").getAsInt();
    }

    public static EmoteRarity getRarity(UUID uuid) {
        JsonElement elem = emotes.get(uuid.toString());
        if (elem == null) {
            return null;
        }

        JsonObject obj = elem.getAsJsonObject();
        if (obj.get("rarity") == null) {
            return null;
        }

        return switch (obj.get("rarity").getAsString()) {
            case "UNCOMMON" -> EmoteRarity.UNCOMMON;
            case "RARE" -> EmoteRarity.RARE;
            case "EPIC" -> EmoteRarity.EPIC;
            default -> EmoteRarity.COMMON;
        };
    }

    private static String getEmoteChatRaw(UUID uuid) {
        return getEmoteChatRaw(uuid, EmoteTextType.RANDOM);
    }

    private static String getEmoteChatRaw(UUID uuid, EmoteTextType type) {
        JsonElement element = emotes.get(uuid.toString());
        if (element == null)
            return null;
        JsonObject obj = element.getAsJsonObject();

        // This happens in the emotes.json file sometimes because
        // the data couldn't be scrapped / is missing which is annoying

        JsonElement message = obj.get("message");
        if (message == null)
            return null;

        switch (type) {
            case RANDOM -> {
                if (emoteRandom.nextInt(0, 16) == 4) {
                    return obj.get("specialmessage").getAsString();
                }

                return message.getAsString();
            }
            case MESSAGE -> {
                return message.getAsString();
            }
            case SPECIAL_MESSAGE -> {
                return obj.get("specialmessage").getAsString();
            }
            default -> {
                return null;
            }
        }
    }

    public static String getEmoteChatString(UUID uuid, ExtrasPlayer player) {
        return getEmoteChatString(uuid, player, EmoteTextType.RANDOM);
    }

    public static String getEmoteChatString(UUID uuid, ExtrasPlayer player, EmoteTextType type) {
        String raw = getEmoteChatRaw(uuid,type);

        // Apparently emotes can just have no text....
        if (raw == null || raw.isEmpty()) {
            return null;
        }


        // It seems like some emotes also use @p for the player's name so we'll replace that too
        // I also don't think this handled by bedrock but idfk anymore
        raw = raw.replace("@p", player.getSession().javaUsername());

        raw = raw.replace("@", player.getSession().javaUsername());


        int amountOfAmpersand = 0;
        int i = 0;
        char[] parsed = raw.toCharArray();
        for (char charRealSmooth : parsed) {
            // Minecraft: Bedrock Edition does not use the '$' character
            // but sometimes emotes have it for some reason???
            // even an official Minecraft emote has it: https://github.com/GeyserExtras/EmoteExtractor/blob/8844b513dc83194ccd9002dceceafca2db8457ba/emotes/en_US.json#L3587
            // so we'll handle it anyway
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

    public enum EmoteTextType {
        RANDOM,
        SPECIAL_MESSAGE,
        MESSAGE
    }
}
