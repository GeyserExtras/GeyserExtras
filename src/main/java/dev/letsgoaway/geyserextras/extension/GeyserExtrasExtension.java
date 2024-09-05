package dev.letsgoaway.geyserextras.extension;

import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;

import java.nio.file.Path;

public class GeyserExtrasExtension implements Extension, Server {
    public static GeyserExtras CORE;
    public static GeyserExtrasExtension EXTENSION;
    private ExtensionTickUtil tickUtil;

    public GeyserExtrasExtension() {
        ServerType.type = IsAvailable.classExists("org.geysermc.geyser.platform.standalone.GeyserStandaloneBootstrap") ? ServerType.STANDALONE : ServerType.EXTENSION;
        EXTENSION = this;
    }

    @Subscribe
    public void onEnable(GeyserPostInitializeEvent ev) {
        CORE = new GeyserExtras(this);
        tickUtil = new ExtensionTickUtil();
    }

    @Override
    public TickUtil getTickUtil() {
        return tickUtil;
    }

    @Override
    public ExtrasPlayer createPlayer(GeyserConnection connection) {
        return new ExtensionExtrasPlayer(connection);
    }

    @Override
    public void log(String string) {
        this.logger().info(string);
    }

    @Override
    public void warn(String string) {
        this.logger().warning(string);
    }

    @Override
    public Path getPluginFolder() {
        return this.dataFolder();
    }
}
