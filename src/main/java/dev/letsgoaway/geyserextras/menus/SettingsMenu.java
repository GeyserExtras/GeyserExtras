package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.Config;
import dev.letsgoaway.geyserextras.form.BedrockForm;
import dev.letsgoaway.geyserextras.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.form.elements.Toggle;

import static dev.letsgoaway.geyserextras.BedrockPlayer.cooldownTypes;

public class SettingsMenu extends BedrockForm {
    SettingsMenu(BedrockPlayer bplayer) {
        super("Settings");
        if (Config.customCoolDownEnabled) {
            add(new Dropdown("Attack Indicator", cooldownTypes, bplayer.cooldownType,
                    bplayer::setCooldownType)
            );
        }
        if (bplayer.player.hasPermission("geyser.command.offhand")) {
            add(new Toggle("Sneak-Drop to Swap Offhand", bplayer.enableSneakDropOffhand, (b) -> {
                bplayer.enableSneakDropOffhand = b;
                bplayer.setEnableSneakDropOffhand(b);
            }));
        }
        add(new Toggle("Arrow Delay Fix", bplayer.enableArrowDelayFix, bplayer::setEnableArrowDelayFix));
    }
}
