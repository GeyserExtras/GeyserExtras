package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import org.cloudburstmc.protocol.bedrock.packet.EntityPickRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockEntityPickRequestTranslator;

@Translator(packet = EntityPickRequestPacket.class)
public class BedrockEntityPickRequestInjector extends PacketTranslator<EntityPickRequestPacket> {
    BedrockEntityPickRequestTranslator translator = new BedrockEntityPickRequestTranslator();

    @Override
    public void translate(GeyserSession session, EntityPickRequestPacket packet) {

        if (true)// to-do config ootion
            session.entities().switchHands();
        else
            translator.translate(session, packet);
    }
}
