package dev.letsgoaway.geyserextras.core;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.core.packets.PacketReceiveHandler;
import dev.letsgoaway.geyserextras.core.packets.PacketSendHandler;
import org.geysermc.api.GeyserApiBase;
import org.geysermc.event.Listener;
import org.geysermc.event.PostOrder;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.lang.annotation.Annotation;
import java.util.HashMap;

public class GeyserExtras implements EventRegistrar {
    public static GeyserExtras GE;

    public static Server SERVER;

    public GeyserApi geyserApi;

    public HashMap<String, ExtrasPlayer> connections;

    public GeyserExtras(Server server) {
        GE = this;
        GeyserExtras.SERVER = server;
        geyserApi = GeyserApi.api();
        Config.load();
        geyserApi.eventBus().subscribe(this, GeyserDefineCommandsEvent.class, this::onDefineCommands);
        geyserApi.eventBus().subscribe(this, GeyserPostInitializeEvent.class, this::onGeyserInitialize);
        geyserApi.eventBus().subscribe(this, ClientEmoteEvent.class, this::onEmoteEvent);
        geyserApi.eventBus().subscribe(this, SessionJoinEvent.class, this::onSessionJoin);
        geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, this::onSessionRemove);
        connections = new HashMap<>();
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(false);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketSendHandler(),
                PacketListenerPriority.HIGHEST);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketReceiveHandler(),
                PacketListenerPriority.HIGHEST);
        PacketEvents.getAPI().init();
    }

    public void tick() {
        for (ExtrasPlayer player : connections.values()) {
            player.tick();
        }
    }

    @Subscribe
    public void onDefineCommands(GeyserDefineCommandsEvent ev) {

    }

    @Subscribe
    public void onGeyserInitialize(GeyserPostInitializeEvent init) {
        SoundReplacer.loadSoundMappings();
    }

    @Subscribe(postOrder = PostOrder.EARLY)
    public void onSessionJoin(SessionJoinEvent ev) {
        connections.put(ev.connection().xuid(), SERVER.createPlayer(ev.connection()));
    }

    @Subscribe(postOrder = PostOrder.EARLY)
    public void onSessionRemove(SessionDisconnectEvent ev) {
        connections.remove(ev.connection().xuid());
    }

    @Subscribe(postOrder = PostOrder.EARLY)
    public void onEmoteEvent(ClientEmoteEvent ev) {
        connections.get(ev.connection().xuid()).onEmoteEvent(ev);
    }

}
