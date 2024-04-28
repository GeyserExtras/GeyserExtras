package dev.letsgoaway.geyserextras;

import dev.letsgoaway.geyserextras.menus.TabList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (GeyserExtras.bedrockAPI.isBedrockPlayer(player.getUniqueId())){
                new TabList(GeyserExtras.bplayers.get(player.getUniqueId()));
            }
            else {
                player.sendMessage("Only Bedrock Players can execute that command!");
            }
        }
        else {
            sender.sendMessage("Only Bedrock Players can execute that command!");
        }
        return true;
    }
}
