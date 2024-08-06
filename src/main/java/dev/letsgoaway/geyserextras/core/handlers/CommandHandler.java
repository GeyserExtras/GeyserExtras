package dev.letsgoaway.geyserextras.core.handlers;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.commands.BedrockCommand;
import dev.letsgoaway.geyserextras.core.commands.GeyserExtrasCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {
    private static Map<String, BedrockCommand> ids;

    public static void loadCommands() {
        ids = new HashMap<>();
        ids.put("ge", new GeyserExtrasCommand());
        ids.put("geyserextras", new GeyserExtrasCommand());
    }

    public static boolean isExtrasCommand(String input) {
        ArrayList<String> args = new ArrayList<>(List.of(input.substring(1).split(" ")));
        String root = args.get(0);
        return ids.containsKey(root);
    }
    public static void runFromInput(ExtrasPlayer player, String input) {
        ArrayList<String> args = new ArrayList<>(List.of(input.substring(1).split(" ")));
        String root = args.get(0);
        if (ids.containsKey(root)) {
            args.remove(0);
            ids.get(root).onExecute(player, args);
        }
    }
}
