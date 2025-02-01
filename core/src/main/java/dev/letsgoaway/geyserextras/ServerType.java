package dev.letsgoaway.geyserextras;

import org.geysermc.geyser.api.util.PlatformType;

public enum ServerType {
    BUNGEE,
    EXTENSION,
    SPIGOT,
    VELOCITY,
    STANDALONE;
    public static ServerType type;
    public static PlatformType platformType;
    public static boolean isExtension() {
        return type == EXTENSION || type == STANDALONE;
    }

    public static ServerType get(){
        return type;
    }

    public static PlatformType platform() {
        return platformType;
    }
}
