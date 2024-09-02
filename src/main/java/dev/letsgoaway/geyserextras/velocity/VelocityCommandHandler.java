package dev.letsgoaway.geyserextras.velocity;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import dev.letsgoaway.geyserextras.core.handlers.CommandHandler;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;

public class VelocityCommandHandler implements RawCommand {
    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player){
            CommandHandler.runFromInput(GeyserHandler.getPlayer(player.getUniqueId()), "/"+invocation.alias()+" "+invocation.arguments());
        }
    }
}
