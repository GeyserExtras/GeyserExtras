package dev.letsgoaway.geyserextras;

public enum ServerType {
    BUNGEE,
    EXTENSION,
    SPIGOT,
    VELOCITY,
    STANDALONE;
    public static ServerType type;

    public static boolean isExtension() {
        return type == EXTENSION || type == STANDALONE;
    }
    public static ServerType get(){
        return type;
    }
}
