package dev.letsgoaway.geyserextras;

public enum ServerType {
    SPIGOT,
    VELOCITY;
    public static ServerType type;
    public static ServerType get(){
        return type;
    }
}
