package dev.letsgoaway.geyserextras;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.io.File;
import java.nio.file.Path;

public interface Server {
    TickUtil getTickUtil();

    ExtrasPlayer createPlayer(GeyserConnection connection);

    void log(String string);

    Path getPluginFolder();
}
