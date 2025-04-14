package dev.letsgoaway.geyserextras.core.injectors.java;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.java.entity.JavaAddEntityTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundAddEntityPacket;

@Translator(packet = ClientboundAddEntityPacket.class)
public class JavaAddEntityInjector extends JavaAddEntityTranslator {
    @Override
    public void translate(GeyserSession session, ClientboundAddEntityPacket packet) {
        super.translate(session, packet);
        if (packet.getType() == EntityType.PLAYER) {
            ExtrasPlayer player = GeyserHandler.getPlayer(session);
            player.getPlayerDimensionsMap().put(packet.getEntityId(), session.getDimensionType());
        }
    }
}
