package dev.letsgoaway.geyserextras.spigot.player;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import org.bukkit.entity.Player;

public enum PlayerPlatform {
    CONSOLE("Console"),
    MOBILE("Mobile"),
    PC("PC"),
    VR("VR"),
    UNKNOWN("Unknown");

    public final String displayName;

    PlayerPlatform(String displayName) {
        this.displayName = displayName;
    }

    public static PlayerPlatform getPlayerPlatform(BedrockPlayer bedrockPlayer) {
        return switch (PlayerDevice.getPlayerDevice(bedrockPlayer)) {
            case ANDROID,IOS,AMAZON,WINDOWS_PHONE -> PlayerPlatform.MOBILE;
            case WINDOWS,OSX,JAVA -> PlayerPlatform.PC;
            case PLAYSTATION,SWITCH,XBOX,TVOS -> PlayerPlatform.CONSOLE;
            case GEARVR,HOLOLENS -> PlayerPlatform.VR;
            default -> PlayerPlatform.UNKNOWN;
        };
    }
    public static PlayerPlatform getPlayerPlatform(Player player) {
        return switch (PlayerDevice.getPlayerDevice(player)) {
            case ANDROID,IOS,AMAZON,WINDOWS_PHONE -> PlayerPlatform.MOBILE;
            case WINDOWS,OSX,JAVA -> PlayerPlatform.PC;
            case PLAYSTATION,SWITCH,XBOX,TVOS -> PlayerPlatform.CONSOLE;
            case GEARVR,HOLOLENS -> PlayerPlatform.VR;
            default -> PlayerPlatform.UNKNOWN;
        };
    }
}
