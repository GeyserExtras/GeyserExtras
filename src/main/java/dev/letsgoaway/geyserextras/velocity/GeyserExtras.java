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
import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.ServerType;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Plugin(id = "geyserextras", name = "GeyserExtras", version = PluginVersion.GE_VERSION,
        description = "A plugin which attempts to unify features for Bedrock Edition players on Java Edition Servers with GeyserMC.", authors = {"LetsGoAway"})
public class GeyserExtras {
    public static ProxyServer server = null;
    @Inject
    public static Logger logger;

    public static InitializeLogger initLog;


    private GeyserEventForwarder forwarder;

    public static ChannelIdentifier emoteChannel = MinecraftChannelIdentifier.create("geyserextras", "emote");
    public static ChannelIdentifier fogChannel = MinecraftChannelIdentifier.create("geyserextras", "fog");

    @Inject
    public GeyserExtras(ProxyServer proxyServer, Logger logger) {
        ServerType.type = ServerType.VELOCITY;
        GeyserExtras.server = proxyServer;
        GeyserExtras.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent ev) {
        initLog = new InitializeLogger((s) -> logger.warn(s), (s) -> logger.info(s));
        initLog.start();
        PluginVersion.checkForUpdatesAndPrintToLog((s) -> logger.warn(s));
        initLog.logTask("Registering channels...", () -> {
            server.getChannelRegistrar().register(emoteChannel);
            server.getChannelRegistrar().register(fogChannel);
        }, "Channels registered!");
        this.forwarder = new GeyserEventForwarder();
        initLog.warn("Make sure that 'proxy-mode: true' on your backend servers GeyserExtras config!");
        initLog.end();
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
