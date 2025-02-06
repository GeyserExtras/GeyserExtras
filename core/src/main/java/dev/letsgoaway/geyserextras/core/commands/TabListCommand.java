package dev.letsgoaway.geyserextras.core.commands;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.parity.java.menus.tablist.PlayerListMenu;

import java.util.List;
import java.util.UUID;

public class TabListCommand implements BedrockCommand {

    @Override
    public void onExecute(ExtrasPlayer player, List<String> args, String label) {
        player.sendForm(new PlayerListMenu());
    }

}
