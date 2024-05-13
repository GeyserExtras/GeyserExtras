package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.ServerType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

public class GeyserExtras extends Plugin {

    public static Logger logger;

    public static InitializeLogger initLog;
    public static ProxyServer proxyServer;

    private GeyserEventForwarder forwarder;

    public static String emoteChannel = "geyserextras:emote";

    public static String fogChannel = "geyserextras:fog";
    public GeyserExtras() {
        ServerType.type = ServerType.BUNGEE;
    }

    @Override
    public void onEnable() {
        proxyServer = ProxyServer.getInstance();
        proxyServer.getPluginManager().registerListener(this, new EventListener());
        logger = this.getLogger();
        initLog = new InitializeLogger((s) -> logger.warning(s), (s) -> logger.info(s));
        initLog.start();
        PluginVersion.checkForUpdatesAndPrintToLog((s) -> logger.warning(s));
        initLog.logTask("Registering channels...", () -> {
            proxyServer.registerChannel(emoteChannel);
            proxyServer.registerChannel(fogChannel);
        }, "Channels registered!");
        this.forwarder = new GeyserEventForwarder();
        initLog.warn("Make sure that 'proxy-mode: true' on your backend servers GeyserExtras config!");
        initLog.end();
    }
    @Override
    public void onDisable() {
        proxyServer.unregisterChannel(emoteChannel);
        proxyServer.unregisterChannel(fogChannel);
    }
}
