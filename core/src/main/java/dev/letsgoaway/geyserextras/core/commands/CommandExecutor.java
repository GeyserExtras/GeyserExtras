package dev.letsgoaway.geyserextras.core.commands;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;

import java.util.*;

public class CommandExecutor {
    public static Map<String, BedrockCommand> ids;

    public static void loadCommands() {
        ids = new HashMap<>();
        add(List.of("ge", "geyserextras"), new GeyserExtrasCommand());
        add(List.of("emotechat", "muteemotechat", "unmuteemotechat"), new EmoteChatCommand());
        add(List.of("l", "players", "listplayers", "tab", "tablist"), new TabListCommand());
    }


    public static void add(List<String> aliases, BedrockCommand cmd) {
        for (String alias : aliases) {
            ids.put(alias, cmd);
        }
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
