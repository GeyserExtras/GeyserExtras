package dev.letsgoaway.geyserextras.core.commands;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.geyser.api.command.Command;


public interface BedrockCommand {
    void onExecute(ExtrasPlayer player, String args);

    void onConsoleExecute(String args);
}
