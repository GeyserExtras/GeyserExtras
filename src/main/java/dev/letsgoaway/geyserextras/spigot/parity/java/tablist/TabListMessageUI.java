package dev.letsgoaway.geyserextras.spigot.parity.java.tablist;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.core.geyser.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.geyser.form.elements.TextInput;
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
