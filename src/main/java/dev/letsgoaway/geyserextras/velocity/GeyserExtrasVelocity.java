package dev.letsgoaway.geyserextras.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.letsgoaway.geyserextras.*;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Plugin(authors = "LetsGoAway", name = "GeyserExtras", version = PluginVersion.GE_VERSION, id = "geyserextras", dependencies = {@Dependency(id = "geyser"),@Dependency(id = "packetevents")})
public class GeyserExtrasVelocity implements Server {
    public static GeyserExtras CORE;
    public static GeyserExtrasVelocity VELOCITY;
    public static ProxyServer server;
    private final Logger logger;

    private final VelocityTickUtil velocityTickUtil;

    private final Path dataDirectory;

    @Inject
    public GeyserExtrasVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        ServerType.type = ServerType.VELOCITY;
        VELOCITY = this;
        GeyserExtrasVelocity.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        velocityTickUtil = new VelocityTickUtil();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CORE = new GeyserExtras(this);
    }

    @Override
    public TickUtil getTickUtil() {
        return velocityTickUtil;
    }

    @Override
    public ExtrasPlayer createPlayer(GeyserConnection connection) {
        return new VelocityExtrasPlayer(connection);
    }

    @Override
    public void log(String string) {
        this.logger.info(string);
    }

    @Override
    public void warn(String string) {
        this.logger.warn(string);
    }

    @Override
    public Path getPluginFolder() {
        return dataDirectory;
    }
}
