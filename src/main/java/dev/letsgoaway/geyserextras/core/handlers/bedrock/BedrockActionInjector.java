package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.BedrockActionTranslator;
import org.geysermc.geyser.util.BlockUtils;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerAction;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerActionPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

@Translator(packet = PlayerActionPacket.class)
public class BedrockActionInjector extends BedrockActionTranslator {
    @Override
    public void translate(GeyserSession session, PlayerActionPacket packet) {
        //SERVER.log("PLAYER ACTION PACKET: " + packet.getAction().name() + " " + packet.getBlockPosition().toString() + " " + packet.getResultPosition().toString() + " " + packet.getFace());
        super.translate(session, packet);
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        switch (packet.getAction()) {
            case START_BREAK -> {
                // Hide the cooldown and get ready to break
                // TODO: java continues the cooldown if theres already one thats currently in progress
                player.getCooldownHandler().setDigTicks(0);
                player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
            }
            case CONTINUE_BREAK -> {
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
                // its set to 5 when InventoryTransactionInjector detects a block break, we shouldnt show the cool down then
                if (player.getCooldownHandler().getDigTicks() != 5) {
                    player.getCooldownHandler().setDigTicks(-1);
                    // Java shows cooldown on block break abort.
                }
            }
            case MISSED_SWING -> {
                player.getCooldownHandler().setDigTicks(-1);
                player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                if (BedrockInventoryTransactionInjector.disableBlocking(session)) {
                    session.getPlayerEntity().updateBedrockMetadata();
                    session.getPlayerEntity().resetAttributes();
                }

            }
        }
    }
}
