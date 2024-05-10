package dev.letsgoaway.geyserextras.spigot.player;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.spigot.GeyserExtras;

public enum PlayerUIProfile {
    CLASSIC("Classic"),
    POCKET("Pocket");
    public final String displayName;

    PlayerUIProfile(String displayName) {
        this.displayName = displayName;
    }

    public static PlayerUIProfile getPlayerUIProfile(BedrockPlayer bedrockPlayer) {
        return GeyserExtras.bedrockAPI.getPlayerUIProfile(bedrockPlayer);
    }
}
