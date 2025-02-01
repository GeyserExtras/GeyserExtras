package dev.letsgoaway.geyserextras.core.handlers;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.commands.BedrockCommand;
import dev.letsgoaway.geyserextras.core.commands.EmoteChatCommand;
import dev.letsgoaway.geyserextras.core.commands.GeyserExtrasCommand;

import java.util.*;

public class CommandHandler {
    public static Map<String, BedrockCommand> ids;

    public static void loadCommands() {
        ids = new HashMap<>();
        ids.put("ge", new GeyserExtrasCommand());
        ids.put("geyserextras", new GeyserExtrasCommand());
        ids.put("emotechat", new EmoteChatCommand());
        ids.put("muteemotechat", new EmoteChatCommand());
        ids.put("unmuteemotechat", new EmoteChatCommand());
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
            ids.get(root).onExecute(player, args, root);
        }
    }

    public static void runFromCommandInput(ExtrasPlayer player, String label, String[] args) {
        if (ids.containsKey(label)) {
            ids.get(label).onExecute(player, Arrays.asList(args), label);
        }
    }

    public static void runFromInput(UUID player, String input) {
        ArrayList<String> args = new ArrayList<>(List.of(input.substring(1).split(" ")));
        String root = args.get(0);
        if (ids.containsKey(root)) {
            args.remove(0);
            ids.get(root).onJavaExecute(player, args, root);
        }
    }

    public static void runFromCommandInput(UUID player, String label, String[] args) {
        if (ids.containsKey(label)) {
            ids.get(label).onJavaExecute(player, Arrays.asList(args), label);
        }
    }

}
