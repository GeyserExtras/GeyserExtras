package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.BedrockActionTranslator;

@Translator(packet = PlayerActionPacket.class)
public class BedrockActionInjector extends BedrockActionTranslator {
    @Override
    public void translate(GeyserSession session, PlayerActionPacket packet) {
        super.translate(session, packet);
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        if (packet.getAction().equals(PlayerActionType.START_BREAK)) {
            session.setLastHitTime(System.currentTimeMillis());
        }
        if (packet.getAction().equals(PlayerActionType.CONTINUE_BREAK)) {
            session.setLastHitTime(System.currentTimeMillis());
        }
        if (packet.getAction().equals(PlayerActionType.BLOCK_CONTINUE_DESTROY)) {
            session.setLastHitTime(System.currentTimeMillis());
        }
        if (packet.getAction().equals(PlayerActionType.STOP_BREAK)) {
            //
        }
    }
}
