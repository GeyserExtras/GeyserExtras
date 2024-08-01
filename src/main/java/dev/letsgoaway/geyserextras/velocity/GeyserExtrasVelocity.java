package dev.letsgoaway.geyserextras.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.letsgoaway.geyserextras.*;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import org.slf4j.Logger;

@Plugin(authors = "LetsGoAway", name = "GeyserExtras", version = PluginVersion.GE_VERSION, id = "GeyserExtras")
public class GeyserExtrasVelocity implements Server {
    public static GeyserExtras CORE;
    public static GeyserExtrasVelocity VELOCITY;
    public static ProxyServer server;
    private final Logger logger;

    private VelocityTickUtil velocityTickUtil;

    @Inject
    public GeyserExtrasVelocity(ProxyServer server, Logger logger) {
        ServerType.type = ServerType.VELOCITY;
        VELOCITY = this;
        GeyserExtrasVelocity.server = server;
        this.logger = logger;
        velocityTickUtil = new VelocityTickUtil();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CORE = new GeyserExtras();
    }

    @Override
    public TickUtil getTickUtil() {
        return velocityTickUtil;
    }
}
