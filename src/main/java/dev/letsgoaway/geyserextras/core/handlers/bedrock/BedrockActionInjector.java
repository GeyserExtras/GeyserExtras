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

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

@Translator(packet = PlayerActionPacket.class)
public class BedrockActionInjector extends BedrockActionTranslator {
    @Override
    public void translate(GeyserSession session, PlayerActionPacket packet) {
        super.translate(session, packet);
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        switch (packet.getAction()) {
            case START_BREAK -> {
                player.getCooldownHandler().setDigTicks(0);
                player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
            }
            case CONTINUE_BREAK, BLOCK_CONTINUE_DESTROY -> {
                player.getCooldownHandler().digTicks++;
                // When digging Java's cooldown resets progress every 200 ms at 20 ticks.
                // while the cooldown is hidden during digging
                if (player.getCooldownHandler().getDigTicks() > 4) {
                    player.getCooldownHandler().setDigTicks(0);
                    player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                }
            }
            case STOP_BREAK -> {
                player.getCooldownHandler().setDigTicks(0);
                // Dont show the cooldown until the next action
                // if the block is broken.
            }
            case ABORT_BREAK -> {
                player.getCooldownHandler().setDigTicks(-1);
                // Java shows cooldown on block break abort.
            }
            case MISSED_SWING -> {
                player.getCooldownHandler().setDigTicks(-1);
                player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
            }
        }
    }
}
