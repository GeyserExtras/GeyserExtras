package dev.letsgoaway.geyserextras.core.preferences.bindings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
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
    SNEAK_DROP,
    SETTINGS;

    public String translate(ExtrasPlayer player) {
        GeyserSession session = player.getSession();
        String locale = session.locale();
        switch (this) {
            case PICK_BLOCK -> {
                return BedrockLocale.KEY.PICK_BLOCK;
            }
            case OPEN_INVENTORY -> {
                return BedrockLocale.KEY.INVENTORY;
            }
            case SNEAK_DROP -> {
                return MinecraftLocale.getLocaleString("key.sneak", locale) + " + " + BedrockLocale.CONTROLLER.DROP;
            }
            case SNEAK_INVENTORY -> {
                return MinecraftLocale.getLocaleString("key.sneak", locale) + " + " + BedrockLocale.KEY.INVENTORY;
            }
            case SETTINGS -> {
                return BedrockLocale.GAME_SETTINGS_SCREEN;
            }
            default -> {
                return this.name();
            }
        }
    }
}
