package dev.letsgoaway.geyserextras.spigot.commands;

import dev.letsgoaway.geyserextras.spigot.GeyserExtrasSpigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeyserExtrasCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (GeyserExtrasSpigot.bedrockAPI.isBedrockPlayer(player.getUniqueId())){
                GeyserExtrasSpigot.bplayers.get(player.getUniqueId()).onGeyserExtrasCommand();
            }
        }
        return true;
    }
}