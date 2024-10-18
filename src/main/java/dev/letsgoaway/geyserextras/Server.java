package dev.letsgoaway.geyserextras;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.io.File;
import java.nio.file.Path;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public interface Server {
    TickUtil getTickUtil();

    ExtrasPlayer createPlayer(GeyserConnection connection);

    void log(String string);

    void warn(String string);

    default void debug(String string) {
        if (GE.getConfig().isDebugMode()) {
            log("[DEBUG] " + string);
        }
    }

    default void debugWarn(String string) {
        if (GE.getConfig().isDebugMode()) {
            warn("[DEBUG] " + string);
        }
    }


    Path getPluginFolder();
}
