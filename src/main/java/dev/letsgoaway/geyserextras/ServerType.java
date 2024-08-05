package dev.letsgoaway.geyserextras;

public enum ServerType {
    BUNGEE,
    EXTENSION,
    SPIGOT,
    VELOCITY;
    public static ServerType type;
    public static ServerType get(){
        return type;
    }
}
