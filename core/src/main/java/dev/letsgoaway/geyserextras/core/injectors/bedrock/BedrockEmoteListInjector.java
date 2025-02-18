package dev.letsgoaway.geyserextras.core.injectors.bedrock;

import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.EmoteListPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockEmoteListTranslator;

@Translator(packet = EmoteListPacket.class)
public class BedrockEmoteListInjector extends BedrockEmoteListTranslator {
    @Override
    public void translate(GeyserSession session, EmoteListPacket packet) {
        GeyserHandler.getPlayer(session).getEmotesList().addAll(packet.getPieceIds());
        super.translate(session, packet);
    }
}
