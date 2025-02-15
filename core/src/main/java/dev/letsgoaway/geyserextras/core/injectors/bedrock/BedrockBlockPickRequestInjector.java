package dev.letsgoaway.geyserextras.core.injectors.bedrock;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockBlockPickRequestTranslator;

@Translator(packet = BlockPickRequestPacket.class)
public class BedrockBlockPickRequestInjector extends BedrockBlockPickRequestTranslator {
    @Override
    public void translate(GeyserSession session, BlockPickRequestPacket packet) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        if (player.getPreferences().isDefault(Remappable.PICK_BLOCK))
            super.translate(session, packet);
        else
            player.getPreferences().runAction(Remappable.PICK_BLOCK);
    }
}
