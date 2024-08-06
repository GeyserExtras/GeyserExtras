package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.BedrockInteractTranslator;

public class BedrockInteractInjector extends PacketTranslator<InteractPacket> {
    BedrockInteractTranslator translator = new BedrockInteractTranslator();

    @Override
    public void translate(GeyserSession session, InteractPacket packet) {
        if (!packet.getAction().equals(InteractPacket.Action.OPEN_INVENTORY)) {
            translator.translate(session, packet);
        } else {
            ExtrasPlayer player = GeyserHandler.getPlayer(session);
            if (player.getPreferences().isDefault(Remappable.OPEN_INVENTORY)) {
                translator.translate(session, packet);
                return;
            }
            Entity entity;
            if (packet.getRuntimeEntityId() == session.getPlayerEntity().getGeyserId()) {
                //Player is not in entity cache
                entity = session.getPlayerEntity();
            } else {
                entity = session.getEntityCache().getEntityByGeyserId(packet.getRuntimeEntityId());
            }
            if (entity == null)
                return;
            player.getPreferences().getAction(Remappable.OPEN_INVENTORY).run(player);
        }
    }
}
