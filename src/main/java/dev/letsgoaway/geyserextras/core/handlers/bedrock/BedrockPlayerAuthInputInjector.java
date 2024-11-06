package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.input.BedrockBlockInteractions;
import dev.letsgoaway.geyserextras.core.parity.java.shield.ShieldUtils;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.geysermc.api.util.InputMode;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.input.BedrockPlayerAuthInputTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerState;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerCommandPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

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
                case START_SPRINTING -> {
                    // Dont allow blocking if we start sprinting
                    if (GE.getConfig().isEnableToggleBlock() && ShieldUtils.getBlocking(session) && ShieldUtils.disableBlocking(session)) {
                        playerEntity.updateBedrockMetadata();
                    }
                }
                case START_SNEAKING -> {
                    if (GE.getConfig().isEnableToggleBlock()) {
                        ServerboundPlayerCommandPacket startSneakPacket = new ServerboundPlayerCommandPacket(playerEntity.getEntityId(), PlayerState.START_SNEAKING);
                        session.sendDownstreamGamePacket(startSneakPacket);


                        ShieldUtils.setSneaking(session, true);
                    }
                }
                case STOP_SNEAKING -> {
                    if (GE.getConfig().isEnableToggleBlock()) {
                        ServerboundPlayerCommandPacket stopSneakPacket = new ServerboundPlayerCommandPacket(playerEntity.getEntityId(), PlayerState.STOP_SNEAKING);
                        session.sendDownstreamGamePacket(stopSneakPacket);

                        ShieldUtils.setSneaking(session, false);
                    }
                }
                case MISSED_SWING -> {
                    player.getCooldownHandler().setDigTicks(-1);
                    player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());

                    if (GE.getConfig().isEnableToggleBlock() && ShieldUtils.disableBlocking(session)) {
                        playerEntity.updateBedrockMetadata();
                    }

                    if (session.inputMode().equals(InputMode.TOUCH)) {
                        player.swingArm();
                    }
                }
            }
        }
    }
}
