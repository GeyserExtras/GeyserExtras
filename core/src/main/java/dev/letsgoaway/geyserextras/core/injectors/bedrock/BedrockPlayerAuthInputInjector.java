package dev.letsgoaway.geyserextras.core.injectors.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import dev.letsgoaway.geyserextras.core.injectors.bedrock.input.BedrockBlockInteractions;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.geysermc.api.util.InputMode;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.input.BedrockPlayerAuthInputTranslator;

@Translator(packet = PlayerAuthInputPacket.class)
// Why is the BedrockPlayerAuthInputTranslator a final class???
// I have to use this to work around that
public class BedrockPlayerAuthInputInjector extends PacketTranslator<PlayerAuthInputPacket> {
    BedrockPlayerAuthInputTranslator translator = new BedrockPlayerAuthInputTranslator();
    @Override
    public void translate(GeyserSession session, PlayerAuthInputPacket packet) {
        translator.translate(session, packet);
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        SessionPlayerEntity playerEntity = session.getPlayerEntity();
        for (PlayerAuthInputData input : packet.getInputData()) {
            switch (input) {
                case PERFORM_BLOCK_ACTIONS -> BedrockBlockInteractions.translate(session, packet.getPlayerActions());
                case MISSED_SWING -> {
                    player.getCooldownHandler().setDigTicks(-1);
                    player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());


                    if (session.inputMode().equals(InputMode.TOUCH)) {
                        player.swingArm();
                    }
                }
            }
        }
    }
}
