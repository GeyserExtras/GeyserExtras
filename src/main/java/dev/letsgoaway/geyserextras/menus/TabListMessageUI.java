package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.form.BedrockForm;
import dev.letsgoaway.geyserextras.form.elements.TextInput;
import org.bukkit.entity.Player;

public class TabListMessageUI extends BedrockForm {
    public TabListMessageUI(Player recipient, BedrockPlayer bedrockPlayer){
        super("");
        onClose = ()->{
            new TabListPlayerDetails(bedrockPlayer, recipient);
        };
        add(new TextInput("Messaging: " + recipient.getName(), (s)->{
            bedrockPlayer.player.performCommand("msg "+recipient.getName()+" "+s);
        }));
    }
}
