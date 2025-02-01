package dev.letsgoaway.geyserextras.core.commands;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;

import java.util.List;
import java.util.UUID;

public interface BedrockCommand {
    void onExecute(ExtrasPlayer player, List<String> args, String label);

    default void onJavaExecute(UUID playerUUID, List<String> args, String label){};
}
