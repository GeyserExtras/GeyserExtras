package dev.letsgoaway.geyserextras.core.handlers.bedrock;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockBlockPickRequestTranslator;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

@Translator(packet = BlockPickRequestPacket.class)
public class BedrockBlockPickRequestInjector extends PacketTranslator<BlockPickRequestPacket> {
    public static BedrockBlockPickRequestTranslator translator = new BedrockBlockPickRequestTranslator();

    @Override
    public void translate(GeyserSession session, BlockPickRequestPacket packet) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        if (player.getPreferences().isDefault(Remappable.PICK_BLOCK))
            translator.translate(session, packet);
        else
            player.getPreferences().runAction(Remappable.PICK_BLOCK);
    }
}
