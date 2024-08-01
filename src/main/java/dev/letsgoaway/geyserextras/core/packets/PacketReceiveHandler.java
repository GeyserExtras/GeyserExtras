package dev.letsgoaway.geyserextras.core.packets;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTickingState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.geyser.api.connection.GeyserConnection;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class PacketReceiveHandler implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent ev) {
        ExtrasPlayer player = getPlayer(ev);
        if (player == null) return;
        switch (ev.getPacketType()) {
            default -> {}
        }
    }

    private ExtrasPlayer getPlayer(ProtocolPacketEvent<?> ev) {
        GeyserConnection connection = GE.geyserApi.connectionByUuid(ev.getUser().getUUID());
        if (connection == null) return null;
        return GE.connections.get(connection.xuid());
    }
}
