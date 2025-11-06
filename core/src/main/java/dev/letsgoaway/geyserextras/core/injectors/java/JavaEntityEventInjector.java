package dev.letsgoaway.geyserextras.core.injectors.java;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.java.entity.JavaEntityEventTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundEntityEventPacket;

public class JavaEntityEventInjector extends JavaEntityEventTranslator {
    @Override
    public void translate(GeyserSession session, ClientboundEntityEventPacket packet) {
        super.translate(session, packet);
        Entity entity = session.getEntityCache().getEntityByJavaId(packet.getEntityId());
        if (entity == null)
            return;
        switch (packet.getEvent()) {
            case PLAYER_SWAP_SAME_ITEM -> {
                if (packet.getEntityId() == session.getPlayerEntity().getEntityId()) {
                    ExtrasPlayer player = ExtrasPlayer.get(session);
                    player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                    player.getCooldownHandler().setDigTicks(-1);
                }
            }
            case LIVING_SHIELD_BLOCK -> {
                // Possible bug in Geyser? Shield doesnt play sound, should make PR
                if (packet.getEntityId() == session.getPlayerEntity().getEntityId()) {
                    PlaySoundPacket playSoundPacket = new PlaySoundPacket();
                    playSoundPacket.setSound("item.shield.block");
                    playSoundPacket.setPosition(entity.getPosition());
                    playSoundPacket.setVolume(0.8f);
                    playSoundPacket.setPitch(1.0f);
                    session.sendUpstreamPacket(playSoundPacket);
                }
            }
        }

    }
}
