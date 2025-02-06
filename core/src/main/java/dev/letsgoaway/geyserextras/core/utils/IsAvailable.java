package dev.letsgoaway.geyserextras.core.utils;

import dev.letsgoaway.geyserextras.ServerType;

// Couldn't think of a good name to call this lol
public class IsAvailable {

    private static boolean FLOODGATE = false;

    // so we dont have to do a class check everytime its required, just preload everything
    // on startup
    private static boolean CLOUDBURST = false;
    private static boolean ADVENTURE = false;

    public static boolean classExists(String clazz) {
        try {
            Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void preload() {
        FLOODGATE = classExists("org.geysermc.floodgate.api.SimpleFloodgateApi");
        CLOUDBURST = classExists("org.cloudburstmc.protocol.bedrock.packet.BedrockPacket");
        ADVENTURE = classExists("net.kyori.adventure.text.Component") || classExists("org.geysermc.geyser.platform.spigot.shaded.net.kyori.adventure.text.Component") || classExists("org.geysermc.geyser.platform.bungeecord.shaded.net.kyori.adventure.text.Component");
    }

    public static boolean floodgate() {
        // For some reason this worked to fix a linkage error, idfk
        if (ServerType.type == ServerType.BUNGEECORD || ServerType.type == ServerType.EXTENSION) {
            return false;
        }
        return FLOODGATE;
    }

    // Used to check if Cloudburst protocol is availible in its usual location.
    // If not, its a modded platform which we dont support atm.
    public static boolean cloudburst() {
        return CLOUDBURST;
    }

    public static boolean adventure() {
        return ADVENTURE;
    }
}
