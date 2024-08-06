package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.event.PostOrder;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.*;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GeyserExtras implements EventRegistrar {
    public static GeyserExtras GE;
    public static Server SERVER;
    public GeyserApi geyserApi;
    public ConcurrentHashMap<String, ExtrasPlayer> connections;

    public GeyserExtras(Server server) {
        GE = this;
        GeyserExtras.SERVER = server;
        GeyserHandler.register();
        InitializeLogger.start();
        geyserApi = GeyserApi.api();
        Config.load();
        geyserApi.eventBus().register(this, this);
        geyserApi.eventBus().subscribe(this, GeyserPostInitializeEvent.class, this::onGeyserInitialize);
        geyserApi.eventBus().subscribe(this, ClientEmoteEvent.class, this::onEmoteEvent);
        geyserApi.eventBus().subscribe(this, SessionLoginEvent.class, this::onSessionLogin);
        geyserApi.eventBus().subscribe(this, SessionJoinEvent.class, this::onSessionJoin);
        geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, this::onSessionRemove);
        connections = new ConcurrentHashMap<>();
        InitializeLogger.end();
    }

    /**
     * Dont use this on proxys, only on servers
     * Tick individually based on tickrate for each player
     * on proxys
     */
    public void serverTick() {
        for (ExtrasPlayer player : connections.values()) {
            player.tick();
        }
    }

    @Subscribe
    public void onGeyserInitialize(GeyserPostInitializeEvent init) {
    }

    public void onSessionLogin(SessionLoginEvent ev) {
        if (connections.containsKey(ev.connection().xuid())) {
            connections.remove(ev.connection().xuid());
        }
        connections.put(ev.connection().xuid(), SERVER.createPlayer(ev.connection()));
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

}
