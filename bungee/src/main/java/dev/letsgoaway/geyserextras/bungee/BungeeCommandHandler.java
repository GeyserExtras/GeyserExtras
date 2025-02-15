package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.commands.CommandExecutor;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommandHandler extends Command {
    public BungeeCommandHandler(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            ExtrasPlayer bedrockPlayer = GeyserHandler.getPlayer(player.getUniqueId());
            if (bedrockPlayer != null) {
                CommandExecutor.runFromCommandInput(bedrockPlayer, this.getName(), args);
            }
            else {
                CommandExecutor.runFromCommandInput(player.getUniqueId(), this.getName(), args);
            }
        }
    }
}
