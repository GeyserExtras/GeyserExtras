package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockInventoryTransactionTranslator;
// I love it when bedrock randomly sends data in random packets
// of which the name they have is completely irrelavant to what
// the data does it really helps me waste time trying to figure
// out why the damage event in BedrockInteractTranslator
// doesnt work :3

// but its ok we still win though
public class BedrockInventoryTransactionInjector extends BedrockInventoryTransactionTranslator {
    @Override
    public void translate(GeyserSession session, InventoryTransactionPacket packet) {
        super.translate(session, packet);
        if (packet.getTransactionType().equals(InventoryTransactionType.ITEM_USE_ON_ENTITY)) {
            Entity entity = session.getEntityCache().getEntityByGeyserId(packet.getRuntimeEntityId());
            if (entity == null)
                return;

            if (packet.getActionType() == 1) {
                ExtrasPlayer player = GeyserHandler.getPlayer(session);
                player.getCooldownHandler().setDigTicks(-1);
                player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
            }
        }
    }
}
