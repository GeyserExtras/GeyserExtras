package dev.letsgoaway.geyserextras.core.parity.java.tablist;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.menus.MainMenu;
import org.geysermc.cumulus.form.SimpleForm;

public class PlayerList extends BedrockMenu {
    @Override
    public SimpleForm create(ExtrasPlayer player) {
        setTitle("Player List");
        add(new Button("View Text", ()->{
            player.sendForm(new TextView());
        }));
        add(new Button("Back", ()->{
            player.sendForm(new MainMenu());
        }));
        return super.create(player);
    }
}
