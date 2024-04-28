package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.Config;
import dev.letsgoaway.geyserextras.form.BedrockForm;
import dev.letsgoaway.geyserextras.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.form.elements.Toggle;

import java.util.Arrays;
import java.util.List;

public class QuickMenuSettings extends BedrockForm {
    QuickMenuSettings(BedrockPlayer bplayer) {
        super("Quick-Menu Actions");
        List<String> actions = Config.quickMenuCommands.keySet().stream().toList();
        add(new Dropdown("Emote #1 Action", actions, getAction(0, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(0, str);
                            bplayer.save();
                        }
                )
        );
        add(new Dropdown("Emote #2 Action", actions, getAction(1, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(1, str);
                            bplayer.save();
                        }
                )
        );
        add(new Dropdown("Emote #3 Action", actions, getAction(2, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(2, str);
                            bplayer.save();
                        }
                )
        );
        add(new Dropdown("Emote #4 Action", actions, getAction(3, bplayer),
                        (str) -> {
                            bplayer.quickMenuActions.set(3, str);
                            bplayer.save();
                        }
                )
        );
    }
    private String getAction(int num, BedrockPlayer bedrockPlayer){
        String name = bedrockPlayer.quickMenuActions.get(num);
        if (Config.quickMenuCommands.containsKey(name)){
            return name;
        }
        else {
            return "None";
        }
    }
}
