package dev.letsgoaway.geyserextras.core.packets;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import net.kyori.adventure.key.Key;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.jetbrains.annotations.Nullable;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class PacketSendHandler implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent ev) {
        ExtrasPlayer player = getPlayer(ev);
        if (player == null) return;
        switch (ev.getPacketType()) {
            case PacketType.Play.Server.UPDATE_ATTRIBUTES ->
                    onUpdateAttributes(player, new WrapperPlayServerUpdateAttributes(ev));
            case PacketType.Play.Server.TICKING_STATE ->
                    onTickRateUpdate(player, new WrapperPlayServerTickingState(ev));
            case PacketType.Play.Server.SOUND_EFFECT -> onSoundEvent(player, new WrapperPlayServerSoundEffect(ev), ev);
            case PacketType.Play.Server.ENTITY_SOUND_EFFECT ->
                    onSoundEvent(player, new WrapperPlayServerEntitySoundEffect(ev), ev);
            default -> {
            }
        }
    }

    private void onUpdateAttributes(ExtrasPlayer player, WrapperPlayServerUpdateAttributes attributes) {
        for (WrapperPlayServerUpdateAttributes.Property properties : attributes.getProperties()) {
            if (properties.getAttribute().equals(Attributes.GENERIC_ATTACK_SPEED)) {
                player.setAttackSpeed(properties.getValue());
                break;
            }
        }
    }

    private void onTickRateUpdate(ExtrasPlayer player, WrapperPlayServerTickingState tickingState) {
        player.setTickingState(tickingState.getTickRate());
    }

    private void onSoundEvent(ExtrasPlayer player, WrapperPlayServerSoundEffect soundEffect, PacketSendEvent ev) {
        if (Config.javaCombatSounds) {
            soundEffect.setSound(SoundReplacer.getSound(soundEffect.getSound().getSoundId().toString()));
            ev.setLastUsedWrapper(soundEffect);
            ev.markForReEncode(true);
        }
    }

    private void onSoundEvent(ExtrasPlayer player, WrapperPlayServerEntitySoundEffect soundEffect, PacketSendEvent ev) {
        if (Config.javaCombatSounds) {
            soundEffect.setSound(SoundReplacer.getSound(soundEffect.getSound().getSoundId().toString()));
            ev.setLastUsedWrapper(soundEffect);
            ev.markForReEncode(true);
        }
    }

    private ExtrasPlayer getPlayer(ProtocolPacketEvent<?> ev) {
        GeyserConnection connection = GE.geyserApi.connectionByUuid(ev.getUser().getUUID());
        if (connection == null) return null;
        return GE.connections.get(connection.xuid());
    }
}
