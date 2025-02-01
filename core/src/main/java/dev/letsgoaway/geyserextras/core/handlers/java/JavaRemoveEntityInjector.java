package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.java.entity.JavaRemoveEntitiesTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundRemoveEntitiesPacket;

public class JavaRemoveEntityInjector extends JavaRemoveEntitiesTranslator {
    @Override
    public void translate(GeyserSession session, ClientboundRemoveEntitiesPacket packet) {
        super.translate(session, packet);
        for (int entityId : packet.getEntityIds()) {
            GeyserHandler.getPlayer(session).getPlayerDimensionsMap().remove(entityId);
        }
    }
}
