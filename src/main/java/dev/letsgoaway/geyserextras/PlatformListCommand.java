package dev.letsgoaway.geyserextras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlatformListCommand implements CommandExecutor {
    private final String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(color("&bThere are a total of &f["+ (long) Bukkit.getServer().getOnlinePlayers().size() +"/"+Bukkit.getServer().getMaxPlayers()+"] &bplayers online&3."));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
            String playerName = onlinePlayer.getName();
            if (GeyserExtras.bedrockAPI.isBedrockPlayer(onlinePlayer.getUniqueId())){
                BedrockPlayer bedrockPlayer = GeyserExtras.bplayers.get(onlinePlayer.getUniqueId());
                sender.sendMessage(color("&3"+playerName+" &8- &eBedrock &7&o["+bedrockPlayer.device.displayName+"]"));
            }
            else {
                sender.sendMessage(color("&3"+playerName+" &8- &eJava"));
            }
        }

        return true;
    }
}