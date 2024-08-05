package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.Server;
import org.geysermc.event.PostOrder;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
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
        InitializeLogger.start();
        geyserApi = GeyserApi.api();
        Config.load();
        geyserApi.eventBus().subscribe(this, GeyserPostInitializeEvent.class, this::onGeyserInitialize);
        geyserApi.eventBus().subscribe(this, ClientEmoteEvent.class, this::onEmoteEvent);
        geyserApi.eventBus().subscribe(this, SessionJoinEvent.class, this::onSessionJoin);
        geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, this::onSessionRemove);
        connections = new HashMap<>();
        if (IsAvailable.packetEvents()) {
            SERVER.log("PacketEvents Detected!");
            dev.letsgoaway.geyserextras.core.handlers.packetevents.PacketEventsHandler.register();
        }
        InitializeLogger.end();
    }

    /**
     * Dont use this on proxys, only on servers
     * Tick individually based on tickrate for each player
     * on proxys
     */
    public void tick() {
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
        connections.put(ev.connection().xuid(), SERVER.createPlayer(ev.connection()));
    }

    @Subscribe(postOrder = PostOrder.FIRST)
    public void onSessionRemove(SessionDisconnectEvent ev) {
        connections.get(ev.connection().xuid()).onDisconnect();
        connections.remove(ev.connection().xuid());
    }

    @Subscribe(postOrder = PostOrder.FIRST)
    public void onEmoteEvent(ClientEmoteEvent ev) {
        connections.get(ev.connection().xuid()).onEmoteEvent(ev);
    }

}
