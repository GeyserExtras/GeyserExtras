package dev.letsgoaway.geyserextras.core;

import org.geysermc.api.util.BedrockPlatform;

public enum PlayerPlatforms {
    UNKNOWN,
    ANDROID,
    IOS,
    MACOS,
    AMAZON,
    WINDOWS,
    PLAYSTATION,
    NINTENDO_SWITCH,
    XBOX,
    LINUX;

    private final String langID;

    PlayerPlatforms() {
        this.langID = "ge.platform." + this.name().toLowerCase();
    }

    public String translate(ExtrasPlayer player) {
        return player.translateGE(langID);
    }

    public static PlayerPlatforms fromGeyser(BedrockPlatform platform) {
        switch (platform) {
            case GOOGLE -> {
                return ANDROID;
            }
            case IOS -> {
                return IOS;
            }
            case OSX -> {
                return MACOS;
            }
            case AMAZON -> {
                return AMAZON;
            }
            case UWP, WIN32, WINDOWS_PHONE -> {
                return WINDOWS;
            }
            case PS4 -> {
                return PLAYSTATION;
            }
            case NX -> {
                return NINTENDO_SWITCH;
            }
            case XBOX -> {
                return XBOX;
            }
            case LINUX -> {
                return LINUX;
            }
            default -> {
                return UNKNOWN;
            }
        }
    }
}
