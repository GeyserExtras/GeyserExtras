package dev.letsgoaway.geyserextras.core.preferences.bindings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;

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
        switch (this) {
            case PICK_BLOCK -> {
                return BedrockLocale.KEY.PICK_BLOCK;
            }
            case OPEN_INVENTORY -> {
                return BedrockLocale.KEY.INVENTORY;
            }
            case SNEAK_DROP -> {
                return player.translate("key.sneak") + " + " + BedrockLocale.CONTROLLER.DROP;
            }
            case EMOTE_1 -> {
                return player.translateGE("ge.settings.bindings.emote1");
            }
            case EMOTE_2 -> {
                return player.translateGE("ge.settings.bindings.emote2");
            }
            case EMOTE_3 -> {
                return player.translateGE("ge.settings.bindings.emote3");
            }
            case EMOTE_4 -> {
                return player.translateGE("ge.settings.bindings.emote4");
            }
            case SNEAK_INVENTORY -> {
                return player.translate("key.sneak") + " + " + BedrockLocale.KEY.INVENTORY;
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
