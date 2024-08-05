package dev.letsgoaway.geyserextras.core.handlers.bedrock;
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockBlockPickRequestTranslator;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

@Translator(packet = BlockPickRequestPacket.class)
public class BedrockBlockPickRequestInjector extends PacketTranslator<BlockPickRequestPacket> {
    BedrockBlockPickRequestTranslator translator = new BedrockBlockPickRequestTranslator();

    @Override
    public void translate(GeyserSession session, BlockPickRequestPacket packet) {
        if (true)// to-do config ootion
            session.entities().switchHands();
        else
            translator.translate(session, packet);
    }
}
