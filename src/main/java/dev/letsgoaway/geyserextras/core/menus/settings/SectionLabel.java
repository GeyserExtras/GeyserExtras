package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.form.elements.Label;
import org.geysermc.geyser.text.ChatColor;

public class SectionLabel extends Label {
    public SectionLabel(String text, String description) {
        super( ChatColor.BOLD + text + "\n" + ChatColor.RESET + ChatColor.ITALIC + ChatColor.DARK_GRAY + description);
    }
}
