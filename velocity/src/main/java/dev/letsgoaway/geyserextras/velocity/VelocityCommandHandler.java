package dev.letsgoaway.geyserextras.velocity;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.CommandHandler;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;

public class VelocityCommandHandler implements RawCommand {
    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            String command = "/" + invocation.alias() + " " + invocation.arguments();

            ExtrasPlayer bedrockPlayer = GeyserHandler.getPlayer(player.getUniqueId());
            if (bedrockPlayer != null) {
                CommandHandler.runFromInput(bedrockPlayer, command);
            } else {
                CommandHandler.runFromInput(player.getUniqueId(), command);
            }
        }
    }
}
