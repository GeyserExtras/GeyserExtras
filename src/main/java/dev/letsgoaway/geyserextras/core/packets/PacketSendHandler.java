package dev.letsgoaway.geyserextras.core.packets;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import dev.letsgoaway.geyserextras.MathUtils;
import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import net.kyori.adventure.key.Key;
import org.bukkit.attribute.AttributeModifier;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.jetbrains.annotations.Nullable;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class PacketSendHandler implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent ev) {
        ExtrasPlayer player = getPlayer(ev);
        // Not a Geyser player
        if (player == null) return;
        if (ev.getPacketType() instanceof PacketType.Play.Server packet) {
            switch (packet) {
                case TICKING_STATE -> onTickRateUpdate(player, new WrapperPlayServerTickingState(ev));
                case SOUND_EFFECT -> onSoundEvent(new WrapperPlayServerSoundEffect(ev), ev);
                case ENTITY_SOUND_EFFECT -> onSoundEvent(new WrapperPlayServerEntitySoundEffect(ev), ev);
                default -> {
                }
            }
        }
    }

    private void onTickRateUpdate(ExtrasPlayer player, WrapperPlayServerTickingState tickingState) {
        player.setTickingState(tickingState.getTickRate());
    }

    private void onSoundEvent(WrapperPlayServerSoundEffect soundEffect, PacketSendEvent ev) {
        if (Config.javaCombatSounds) {
            soundEffect.setSound(SoundReplacer.getSound(soundEffect.getSound().getSoundId().toString()));
            ev.setLastUsedWrapper(soundEffect);
            ev.markForReEncode(true);
        }
    }

    private void onSoundEvent(WrapperPlayServerEntitySoundEffect soundEffect, PacketSendEvent ev) {
        if (Config.javaCombatSounds) {
            soundEffect.setSound(SoundReplacer.getSound(soundEffect.getSound().getSoundId().toString()));
            ev.setLastUsedWrapper(soundEffect);
            ev.markForReEncode(true);
        }
    }

    private ExtrasPlayer getPlayer(ProtocolPacketEvent<?> ev) {
        try {
            GeyserConnection connection = GE.geyserApi.connectionByUuid(ev.getUser().getUUID());
            if (connection == null) return null;
            return GE.connections.get(connection.xuid());
        } catch (NullPointerException e) {
            return null;
        }
    }
}
