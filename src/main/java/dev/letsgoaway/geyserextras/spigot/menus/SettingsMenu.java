package dev.letsgoaway.geyserextras.spigot.menus;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.form.BedrockForm;
import dev.letsgoaway.geyserextras.spigot.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.spigot.form.elements.Toggle;

public class SettingsMenu extends BedrockForm {
    SettingsMenu(BedrockPlayer bplayer) {
        super("Settings");
        if (Config.customCoolDownEnabled) {
            add(new Dropdown("Attack Indicator", BedrockPlayer.cooldownTypes, bplayer.cooldownType,
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
