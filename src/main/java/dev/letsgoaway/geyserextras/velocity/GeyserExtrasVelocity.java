package dev.letsgoaway.geyserextras.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.VersionConstants;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Plugin(id = "geyserextras", name = "GeyserExtras", version = VersionConstants.PLUGIN_VERSION,
        description = "A plugin which attempts to unify features for Bedrock Edition players on Java Edition Servers with GeyserMC.", authors = {"LetsGoAway"})
public class GeyserExtrasVelocity {
    public static ProxyServer server = null;
    @Inject
    public static Logger logger;

    private GeyserEventForwarder forwarder;

    public static ChannelIdentifier emoteChannel = MinecraftChannelIdentifier.create("geyserextras", "emote");
    public static ChannelIdentifier fogChannel = MinecraftChannelIdentifier.create("geyserextras", "fog");

    @Inject
    public GeyserExtrasVelocity(ProxyServer proxyServer, Logger logger) {
        ServerType.type = ServerType.VELOCITY;
        GeyserExtrasVelocity.server = proxyServer;
        GeyserExtrasVelocity.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent ev) {
        Instant start = Instant.now();
        logger.info("--------------GeyserExtras--------------");
        logger.info("Version: " + VersionConstants.PLUGIN_VERSION);
        logger.info("Server Type: " + ServerType.get());
        logger.info("Registering channels...");
        server.getChannelRegistrar().register(emoteChannel);
        server.getChannelRegistrar().register(fogChannel);
        logger.info("Channels registered!");
        this.forwarder = new GeyserEventForwarder();
        logger.warn("Make sure that 'proxy-mode: true' on your backend servers GeyserExtras config!");
        DecimalFormat r3 = new DecimalFormat("0.000");
        Instant finish = Instant.now();
        logger.info("Done! (" + r3.format(Duration.between(start, finish).toMillis() / 1000.0d) + "s)");
        logger.info("----------------------------------------");

    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent ev) {
        if (ev.getIdentifier().equals(fogChannel) && GeyserEventForwarder.enableNetherFog) {
            if (ev.getSource() instanceof ServerConnection serverConnection) {
                String data = new String(ev.getData(), StandardCharsets.UTF_8);
                String fogID = data.substring(1);
                if (data.charAt(0) == 'r') {
                    Objects.requireNonNull(GeyserEventForwarder.api.connectionByUuid(serverConnection.getPlayer().getUniqueId())).camera().removeFog(fogID);
                } else {
                    Objects.requireNonNull(GeyserEventForwarder.api.connectionByUuid(serverConnection.getPlayer().getUniqueId())).camera().sendFog(fogID);
                }
            }
        }
    }
}
