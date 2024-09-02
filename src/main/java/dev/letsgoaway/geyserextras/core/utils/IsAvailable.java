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
}
