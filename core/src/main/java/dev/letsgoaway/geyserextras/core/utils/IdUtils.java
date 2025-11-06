package dev.letsgoaway.geyserextras.core.utils;

import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class IdUtils {
    /**
     * Returns if the uuid is a Floodgate one.
     *
     * @param uuid UUID
     */
    public static boolean isFloodgateID(UUID uuid) {
        // This is how Geyser detects floodgate id's.
        return uuid.version() == 0;
    }

    public static boolean isBedrockPlayer(UUID uuid) {
        // Java users cannot log in as floodgate users.
        if (isFloodgateID(uuid)) {
            return true;
        }
        // Otherwise check if a bedrock user has signed in as a Java user.
        return GE.geyserApi.isBedrockPlayer(uuid);
    }

    /**
     * Returns the XUID from the UUID as a long.
     * Returns `-1` if it is not a floodgate ID.
     *
     * @param uuid UUID
    */
    public static long getBedrockXUID(UUID uuid) {
        if (!isFloodgateID(uuid)) {
            return -1;
        }
        // This results in an xuid, for example my XUID is 2535430477181905.
        // Floodgate just encodes the XUID into the UUID.
        return uuid.getLeastSignificantBits();
    }
}
