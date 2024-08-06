package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.EmoteListPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockEmoteListTranslator;

public class BedrockEmoteListInjector extends BedrockEmoteListTranslator {
    @Override
    public void translate(GeyserSession session, EmoteListPacket packet) {
        GeyserHandler.getPlayer(session).setEmotesList(packet.getPieceIds());
        super.translate(session, packet);
    }
}
