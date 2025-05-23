package dev.letsgoaway.geyserextras;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.utils.TickUtil;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.nio.file.Path;
import java.util.UUID;

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

    default void sendEmoteChat(ExtrasPlayer player, String emoteChat) {
    }

    default void sendRawMessage(UUID javaPlayer, String message) {}

    default void sendMessage(UUID javaPlayer, String message) {

    }

    Path getPluginFolder();

    default void onGeyserExtrasInitialize() {}

    default void onConfigLoad() {}
}
