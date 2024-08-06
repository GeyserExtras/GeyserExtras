package dev.letsgoaway.geyserextras.core.features.bindings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.MinecraftLocale;

public enum Remappable {
    EMOTE_1,
    EMOTE_2,
    EMOTE_3,
    EMOTE_4,
    SNEAK_INVENTORY,
    OPEN_INVENTORY,
    PICK_BLOCK,
    SNEAK_DROP;

    public String getLocaleString(ExtrasPlayer player) {
        GeyserSession session = player.getSession();
        String locale = session.locale();
        switch (this) {
            case PICK_BLOCK -> {
                return MinecraftLocale.getLocaleString("key.pickItem", locale);
            }
            case OPEN_INVENTORY -> {
                return MinecraftLocale.getLocaleString("container.inventory", locale);
            }
            case SNEAK_DROP -> {
                return MinecraftLocale.getLocaleString("key.sneak", locale) + " + " + MinecraftLocale.getLocaleString("key.drop", locale);
            }
            case SNEAK_INVENTORY -> {
                return MinecraftLocale.getLocaleString("key.sneak", locale) + " + " + MinecraftLocale.getLocaleString("container.inventory", locale);
            }
            default -> {
                return this.name();
            }
        }
    }
}
