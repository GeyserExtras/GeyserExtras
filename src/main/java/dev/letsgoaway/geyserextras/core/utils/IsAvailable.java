package dev.letsgoaway.geyserextras.core.utils;

// Couldn't think of a good name to call this lol
public class IsAvailable {

    public static boolean classExists(String clazz) {
        try {
            Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    // so we dont have to do a class check everytime its required, just preload everything
    // on startup

    public static void preload() {
        FLOODGATE = classExists("org.geysermc.floodgate.api.SimpleFloodgateApi");
        CLOUDBURST = classExists("org.cloudburstmc.protocol.bedrock.packet.BedrockPacket");
        ADVENTURE = classExists("net.kyori.adventure.text.Component");
    }

    private static boolean FLOODGATE = false;

    public static boolean floodgate() {
        return FLOODGATE;
    }

    private static boolean CLOUDBURST = false;

    // Used to check if Cloudburst protocol is availible in its usual location.
    // If not, its a modded platform which we dont support atm.
    public static boolean cloudburst() {
        return CLOUDBURST;
    }

    private static boolean ADVENTURE = false;

    public static boolean adventure() {
        return ADVENTURE;
    }
}
