package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.core.handlers.CommandHandler;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
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
            CommandHandler.runFromCommandInput(GeyserHandler.getPlayer(player.getUniqueId()), this.getName(), args);
        }
    }
}
