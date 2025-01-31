package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.CommandHandler;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player spigotPlayer) {
            ExtrasPlayer player = GeyserHandler.getPlayer(spigotPlayer.getUniqueId());
            if (player != null) {
                CommandHandler.runFromCommandInput(player, label, args);
            }
        }
        return true;
    }
}
