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

    public static boolean floodgate() {
        return classExists("org.geysermc.floodgate.api.SimpleFloodgateApi");
    }

    // Used to check if Cloudburst protocol is availible in its usual location.
    // If not, its a modded platform which we dont support atm.
    public static boolean cloudburst() {
        return classExists("org.cloudburstmc.protocol.bedrock.packet.BedrockPacket");
    }
}
