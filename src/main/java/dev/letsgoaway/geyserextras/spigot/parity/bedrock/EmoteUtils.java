package dev.letsgoaway.geyserextras.spigot.parity.bedrock;

import com.google.gson.Gson;
import dev.letsgoaway.geyserextras.spigot.GeyserExtras;
import dev.letsgoaway.geyserextras.spigot.commands.EmoteChatCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class EmoteUtils {
    public static List<Emote> emotes;

    public static void load() {
        try {
            emotes = Arrays.asList(new Gson().fromJson(new String(Objects.requireNonNull(GeyserExtras.plugin.getResource("emotes.json")).readAllBytes(), StandardCharsets.UTF_8), Emote[].class));
        } catch (Exception ignored) {
        }
    }

    public static String getEmoteName(String uuid) {
        if (uuid.isEmpty() || uuid.isBlank()) {
            return "Not Set";
        }
        for (Emote emote : emotes) {
            if (emote.uuid.equals(uuid)) {
                return emote.name;
            }
        }
        return "Unknown (" + uuid + ")";
    }

    private static Random emoteChatRandom = new Random();

    private static String getEmoteChatStringUnparsed(String uuid) {
        for (Emote emote : emotes) {
            if (emote.uuid.equals(uuid)) {
                if (emoteChatRandom.nextInt(0, 16) == 4) {
                    return emote.specialmessage;
                } else {
                    return emote.message;
                }
            }
        }
        return "";
    }

    private static String chatColored(final String string) {
        return ChatColor.translateAlternateColorCodes('§', string);
    }

    public static String getEmoteChatString(String uuid, String playerName) {
        String raw = getEmoteChatStringUnparsed(uuid);
        if (raw.isEmpty()){
            return "";
        }
        raw = raw.replace("@", playerName);
        int amountOfAmpersand = 0;
        int i = 0;
        char[] parsed = raw.toCharArray();
        for (char charRealSmooth : parsed) {
            if (charRealSmooth == '&') {
                amountOfAmpersand++;
                if (amountOfAmpersand == 1) {
                    parsed[i] = '\uE001';
                } else if (amountOfAmpersand == 2) {
                    parsed[i] = '\uE002';
                }
            }
            i++;
        }
        raw = new String(parsed);
        raw = raw.replace("\uE001", "§r§a");
        raw = raw.replace("\uE002", "§r");
        return chatColored(raw);
    }

    public static void sendEmoteChat(Player player, String emoteID) {
        String emoteString = getEmoteChatString(emoteID, player.getName());
        if (emoteString.isBlank()){
            return;
        }
        for (Player playerNear : player.getWorld().getEntitiesByClass(Player.class)) {
            if (EmoteChatCommand.getEnabled(playerNear) && !GeyserExtras.bedrockAPI.isBedrockPlayer(playerNear.getUniqueId())
                    && 128.0 >= player.getLocation().distance(playerNear.getLocation())) {
                playerNear.sendRawMessage(emoteString);
            }
        }
    }
}
