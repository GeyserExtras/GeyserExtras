package dev.letsgoaway.geyserextras.core.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.player.User;
import dev.letsgoaway.geyserextras.core.protocol.handlers.workarounds.MannequinCapeWorkaround;

import javax.annotation.Nullable;
import java.util.*;

// This contains work arounds for Java only clients
public class ProtocolHandler implements PacketListener {
    static ProtocolHandler PE;

    static Map<UUID, JavaPlayer> connections = new HashMap<>();

    public static void load(PacketEventsAPI<?> builder) {
        PacketEvents.setAPI(builder);
        PacketEvents.getAPI().load();
        ProtocolHandler.PE = new ProtocolHandler();
        PacketEvents.getAPI().getEventManager().registerListener(ProtocolHandler.PE, PacketListenerPriority.HIGHEST);
    }

    public static void init() {
        PacketEvents.getAPI().init();
        CapeLoader.init();
    }

    public static void terminate() {
        PacketEvents.getAPI().terminate();
    }

    @Nullable
    public static JavaPlayer getJavaPlayer(UUID uuid) {
        return connections.get(uuid);
    }

    public static boolean javaPlayerExists(UUID uuid) {
        return connections.containsKey(uuid);
    }

    public static void add(User user) {
        if (!javaPlayerExists(user.getUUID())) {
            connections.put(user.getUUID(), new JavaPlayer(user));
        }
    }

    public static void remove(User user) {
        connections.remove(user.getUUID());
    }

    @Override
    public void onUserLogin(UserLoginEvent event) {
        add(event.getUser());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        remove(event.getUser());
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        JavaPlayer player = getJavaPlayer(event.getUser().getUUID());

        // Only handle this when we are sending packets to java players
        // Bedrock players get their packets in core/injectors/
        if (player == null) {
            return;
        }

        player.onPacketSend(event);
    }


    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        JavaPlayer player = getJavaPlayer(event.getUser().getUUID());

        // Only handle this when we are sending packets to java players
        // Bedrock players get their packets in core/injectors/
        if (player == null) {
            return;
        }

        player.onPacketReceive(event);
    }
}
