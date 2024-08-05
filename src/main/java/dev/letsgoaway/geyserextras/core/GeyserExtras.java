package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.event.PostOrder;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.*;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;

import java.util.HashMap;

public class GeyserExtras implements EventRegistrar {
    public static GeyserExtras GE;

    public static Server SERVER;

    public GeyserApi geyserApi;

    public HashMap<String, ExtrasPlayer> connections;

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
        geyserApi.eventBus().subscribe(this, SessionJoinEvent.class, this::onSessionJoin);
        geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, this::onSessionRemove);
        connections = new HashMap<>();
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
        SoundReplacer.loadSoundMappings();
    }

    @Subscribe(postOrder = PostOrder.FIRST)
    public void onSessionJoin(SessionJoinEvent ev) {
        if (connections.containsKey(ev.connection().xuid())) {
            connections.remove(ev.connection().xuid());
        }
        connections.put(ev.connection().xuid(), SERVER.createPlayer(ev.connection()));
    }

    @Subscribe(postOrder = PostOrder.FIRST)
    public void onSessionRemove(SessionDisconnectEvent ev) {
        if (connections.containsKey(ev.connection().xuid())) {
            connections.get(ev.connection().xuid()).onDisconnect();
        }
        if (connections.remove(ev.connection().xuid()) == null) {
            SERVER.warn("Could not remove user.");
        }

    }

    @Subscribe(postOrder = PostOrder.FIRST)
    public void onEmoteEvent(ClientEmoteEvent ev) {
        connections.get(ev.connection().xuid()).onEmoteEvent(ev);
    }

}
