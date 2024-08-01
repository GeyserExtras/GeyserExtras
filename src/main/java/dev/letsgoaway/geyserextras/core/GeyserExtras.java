package dev.letsgoaway.geyserextras.core;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.core.packets.PacketSendHandler;
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

    public GeyserApi geyserApi = GeyserApi.api();

    public HashMap<String, ExtrasPlayer> connections = new HashMap<>();

    public GeyserExtras() {
        GE = this;
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(false);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketSendHandler(),
                PacketListenerPriority.HIGH);
    }

    public GeyserExtras(Server server) {
        super();
        SERVER = server;
        SoundReplacer.loadSoundMappings();
    }

    public void tick() {
        for (ExtrasPlayer player : connections.values()) {
            player.tick();
        }
    }

    @Subscribe
    public void onGeyserInitialize(GeyserPostInitializeEvent init) {
        geyserApi.eventBus().subscribe(this, SessionJoinEvent.class, (ev) -> {
            connections.put(ev.connection().xuid(), new ExtrasPlayer(ev.connection()));
        });
        geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, (ev) -> {
            connections.remove(ev.connection().xuid());
        });
        geyserApi.eventBus().subscribe(this, ClientEmoteEvent.class, (ev) -> {
            connections.get(ev.connection().xuid()).onEmoteEvent(ev);
        });
    }
}
