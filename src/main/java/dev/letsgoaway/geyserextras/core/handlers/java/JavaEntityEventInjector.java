package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.core.SoundReplacer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.java.entity.JavaEntityEventTranslator;
import org.geysermc.geyser.util.SoundUtils;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;
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
                    GeyserHandler.getPlayer(session).getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                }
            }
            case LIVING_SHIELD_BLOCK -> {
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
