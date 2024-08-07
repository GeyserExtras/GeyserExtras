package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.BedrockInteractTranslator;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class BedrockInteractInjector extends BedrockInteractTranslator {
    @Override
    public void translate(GeyserSession session, InteractPacket packet) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        if (!packet.getAction().equals(InteractPacket.Action.OPEN_INVENTORY)) {
            if (Config.customCoolDownEnabled) {
                // seems like this is handled properly in BedrockInventoryTransactionTranslator
                // but ill handle it here anyway
                if (packet.getAction().equals(InteractPacket.Action.DAMAGE)) {
                    player.getCooldownHandler().setDigTicks(-1);
                    player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                }
                else if (packet.getAction().equals(InteractPacket.Action.MOUSEOVER)) {
                    player.getCooldownHandler().setLastMouseoverID(packet.getRuntimeEntityId());
                }
            }
            super.translate(session, packet);
        } else {
            Remappable bind = player.getSession().isSneaking() ? Remappable.SNEAK_INVENTORY : Remappable.OPEN_INVENTORY;
            if (player.getPreferences().isDefault(bind)) {
                super.translate(session, packet);
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
            player.getPreferences().getAction(bind).run(player);
        }
    }
}
