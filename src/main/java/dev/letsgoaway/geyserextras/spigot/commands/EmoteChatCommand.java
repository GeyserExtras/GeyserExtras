package dev.letsgoaway.geyserextras.spigot.commands;

import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.GeyserExtrasSpigot;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EmoteChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!GeyserExtrasSpigot.bedrockAPI.isBedrockPlayer(player.getUniqueId())) {
                setEnabled(player, !getEnabled(player));
                if (getEnabled(player)){
                    player.sendRawMessage("Emote chat is now unmuted.");
                }
                else {
                    player.sendRawMessage("Emote chat is now muted.");
                }
            }
            else {
                player.sendRawMessage("You can mute Emote Chat in the Chat menu.");
            }
        }
        return true;
    }

    public static boolean getEnabled(Player player) {
        if (hasData("emotechat", player)) {
          return getData("emotechat", PersistentDataType.BOOLEAN, player);
        }
        return Config.muteEmoteChat;
    }
    public static void setEnabled(Player player, boolean b) {
        setData("emotechat", PersistentDataType.BOOLEAN, b, player);
    }
    private static PersistentDataContainer playerSaveData(Player player) {
        return player.getPersistentDataContainer();
    }

    private static boolean hasData(String key, Player player) {
        try {
            return playerSaveData(player).has(NamespacedKey.fromString(key, GeyserExtrasSpigot.plugin));
        } catch (Exception ignored) {
            return false;
        }
    }

    private static <P, C> void setData(String key, PersistentDataType<P, C> type, C value, Player player) {
        playerSaveData(player).set(NamespacedKey.fromString(key, GeyserExtrasSpigot.plugin), type, value);
    }


    private static <P, C> C getData(String key, PersistentDataType<P, C> type, Player player) {
        return playerSaveData(player).get(NamespacedKey.fromString(key, GeyserExtrasSpigot.plugin), type);
    }
}
