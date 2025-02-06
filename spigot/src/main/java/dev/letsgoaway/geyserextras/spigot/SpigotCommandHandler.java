package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.commands.CommandExecutor;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommandHandler implements org.bukkit.command.CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player spigotPlayer) {
            ExtrasPlayer player = GeyserHandler.getPlayer(spigotPlayer.getUniqueId());
            if (player != null) {
                CommandExecutor.runFromCommandInput(player, label, args);
            }
            else {
                CommandExecutor.runFromCommandInput(spigotPlayer.getUniqueId(), label, args);
            }
        }
        return true;
    }
}
