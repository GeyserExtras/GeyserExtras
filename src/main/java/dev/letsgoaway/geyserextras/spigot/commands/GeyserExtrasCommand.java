package dev.letsgoaway.geyserextras.spigot.commands;

import dev.letsgoaway.geyserextras.spigot.GeyserExtras;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeyserExtrasCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (GeyserExtras.bedrockAPI.isBedrockPlayer(player.getUniqueId())){
                GeyserExtras.bplayers.get(player.getUniqueId()).onGeyserExtrasCommand();
            }
        }
        return true;
    }
}