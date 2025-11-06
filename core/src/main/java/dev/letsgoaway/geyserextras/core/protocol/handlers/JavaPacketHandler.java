package dev.letsgoaway.geyserextras.core.protocol.handlers;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.letsgoaway.geyserextras.core.protocol.JavaPlayer;

public interface JavaPacketHandler {
    void onPacketReceive(JavaPlayer player, PacketReceiveEvent event);
    void onPacketSend(JavaPlayer player, PacketSendEvent event);
    boolean runIfCancelled();
}
