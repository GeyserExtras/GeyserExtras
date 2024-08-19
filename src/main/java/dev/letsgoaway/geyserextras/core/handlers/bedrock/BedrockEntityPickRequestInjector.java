package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.EntityPickRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockEntityPickRequestTranslator;

@Translator(packet = EntityPickRequestPacket.class)
public class BedrockEntityPickRequestInjector extends BedrockEntityPickRequestTranslator {

    @Override
    public void translate(GeyserSession session, EntityPickRequestPacket packet) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        if (player.getPreferences().isDefault(Remappable.PICK_BLOCK))
            super.translate(session, packet);
        else
            player.getPreferences().runAction(Remappable.PICK_BLOCK);
    }
}
