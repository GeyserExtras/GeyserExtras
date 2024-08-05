package dev.letsgoaway.geyserextras.core.handlers.packetevents;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.connection.GeyserConnection;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class PacketReceiveHandler implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent ev) {
        ExtrasPlayer player = getPlayer(ev);
        if (player == null) return;
        if (ev.getPacketType() instanceof PacketType.Play.Client packet) {
            switch (packet) {
                case INTERACT_ENTITY -> {
                    WrapperPlayClientInteractEntity action = new WrapperPlayClientInteractEntity(ev);
                    if (action.getAction().equals(WrapperPlayClientInteractEntity.InteractAction.ATTACK)) {
                        player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                    }
                }
                default -> {}
            }
        }
    }

    private ExtrasPlayer getPlayer(ProtocolPacketEvent<?> ev) {
        GeyserConnection connection = GE.geyserApi.connectionByUuid(ev.getUser().getUUID());
        if (connection == null) return null;
        return GE.connections.get(connection.xuid());
    }
}
