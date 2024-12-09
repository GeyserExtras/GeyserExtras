package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.*;
import dev.letsgoaway.geyserextras.core.cache.Cache;
import dev.letsgoaway.geyserextras.core.cache.PackCacheUtils;
import dev.letsgoaway.geyserextras.core.config.ConfigLoader;
import dev.letsgoaway.geyserextras.core.config.GeyserExtrasConfig;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.preferences.PreferencesData;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import lombok.Getter;
import lombok.Setter;

import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.*;
import org.geysermc.geyser.api.event.lifecycle.*;

import java.util.concurrent.ConcurrentHashMap;

public class GeyserExtras implements EventRegistrar {
    public static GeyserExtras GE;
    public static Server SERVER;

    @Getter
    @Setter
    private GeyserExtrasConfig config;
    public GeyserApi geyserApi;
    public ConcurrentHashMap<String, ExtrasPlayer> connections;

    public GeyserExtras(Server server) {
        GE = this;
        SERVER = server;
        ServerType.platformType = GeyserImpl.getInstance().getPlatformType();

        IsAvailable.preload();

        InitializeLogger.start();

        if (!IsAvailable.cloudburst()) {
            SERVER.warn("!ERROR! GeyserExtras currently does not support running as an extension on modded platforms. !ERROR!");
            SERVER.warn("Please use Geyser-Standalone!");
            InitializeLogger.endNoDone();
            return;
        }

        if (ServerType.isExtension()) {
            GeyserHandler.register();

            SERVER.log("Initializing cache...");
            Cache.initialize();
        }

        geyserApi = GeyserApi.api();

        SERVER.log("Loading config...");
        ConfigLoader.load();

        PreferencesData.init();

        SERVER.log("Registering events...");
        geyserApi.eventBus().register(this, this);
        geyserApi.eventBus().subscribe(this, GeyserPostInitializeEvent.class, this::onGeyserInitialize);

        // ExtrasPlayer handlers
        geyserApi.eventBus().subscribe(this, SessionLoginEvent.class, this::onSessionLogin);
        geyserApi.eventBus().subscribe(this, SessionJoinEvent.class, this::onSessionJoin);
        geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, this::onSessionRemove);

        // Emote bindings
        geyserApi.eventBus().subscribe(this, ClientEmoteEvent.class, this::onEmoteEvent);

        // Auto reconnect
        geyserApi.eventBus().subscribe(this, GeyserPreReloadEvent.class, this::onGeyserReload);
        geyserApi.eventBus().subscribe(this, GeyserShutdownEvent.class, this::onGeyserShutdown);

        // Packs
        geyserApi.eventBus().subscribe(this, SessionLoadResourcePacksEvent.class, this::onLoadPacks);
        connections = new ConcurrentHashMap<>();
        if (ServerType.isExtension()) {
            InitializeLogger.end();
        }

        PluginVersion.checkForUpdatesAndPrintToLog();
    }

    /**
     * Dont use this on proxys, only on servers
     * Tick individually based on tickrate for each player
     * on proxys
     */
    public void serverTick() {
        for (ExtrasPlayer player : connections.values()) {
            if (player.isLoggedIn()) {
                player.tick();
            }
        }
    }

    public void onGeyserInitialize(GeyserPostInitializeEvent init) {
        if (!ServerType.isExtension()) {
            GeyserHandler.register();
            SERVER.log("Initializing cache...");
            Cache.initialize();
            InitializeLogger.end();
        }
    }

    public void onSessionLogin(SessionLoginEvent ev) {

    }

    public void onSessionJoin(SessionJoinEvent ev) {
        connections.get(ev.connection().xuid()).startGame();
    }

    public void onSessionRemove(SessionDisconnectEvent ev) {
        for (ExtrasPlayer player : connections.values()) {
            if (player.getSession().bedrockUsername().equals(ev.connection().bedrockUsername())) {
                connections.get(player.getBedrockXUID()).onDisconnect();
                connections.remove(player.getBedrockXUID());
                return;
            }
        }
        if (connections.containsKey(ev.connection().xuid())) {
            connections.get(ev.connection().xuid()).onDisconnect();
        }
        if (connections.remove(ev.connection().xuid()) == null) {
            SERVER.warn("Could not remove user.");
        }

    }

    public void onEmoteEvent(ClientEmoteEvent ev) {
        connections.get(ev.connection().xuid()).onEmoteEvent(ev);
    }

    public void onGeyserReload(GeyserPreReloadEvent ignored) {
        if (GE.getConfig().isAutoReconnect()) {
            for (ExtrasPlayer player : connections.values()) {
                player.reconnect();
            }
        }
    }

    public void onGeyserShutdown(GeyserShutdownEvent ignored) {
        if (GE.getConfig().isAutoReconnect()) {
            for (ExtrasPlayer player : connections.values()) {
                player.reconnect();
            }
        }
    }

    public void onLoadPacks(SessionLoadResourcePacksEvent ev) {
        connections.remove(ev.connection().xuid());
        connections.put(ev.connection().xuid(), SERVER.createPlayer(ev.connection()));
        PackCacheUtils.onPackLoadEvent(connections.get(ev.connection().xuid()), ev);
    }
}
