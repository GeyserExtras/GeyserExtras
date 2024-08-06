package dev.letsgoaway.geyserextras.core.commands;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;

import java.util.List;

public interface BedrockCommand {
    void onExecute(ExtrasPlayer player, List<String> args);
}
