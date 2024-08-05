package dev.letsgoaway.geyserextras.core;

// Couldn't think of a good name to call this lol
public class IsAvailable {

    private static boolean classExists(String clazz) {
        try {
            Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
