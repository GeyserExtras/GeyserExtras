package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
import dev.letsgoaway.geyserextras.core.menus.SettingsMenu;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;

public class VRSection {
    public static void build(SettingsMenu menu, GeyserSession session, ExtrasPlayer player) {

        menu.add(new Label(ChatColor.BOLD + "VR\n"));
        menu.add(new Toggle("Enable VR Quick-Menu on Inventory Double Click", player.getPreferences().isEnableDoubleClickForVRQuickMenu(), (b) -> {
            player.getPreferences().setEnableDoubleClickForVRQuickMenu(b);
        }));

        menu.add(new Slider("VR Quick-Menu Double Click Time (ms)", 150, 350, 10, player.getPreferences().getVrMenuDoubleClickMS(),
                (i) -> {
                    player.getPreferences().setVrMenuDoubleClickMS(Math.round(i));
                }));
    }
}
