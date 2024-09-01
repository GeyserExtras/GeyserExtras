package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.parity.java.shield.ShieldUtils;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.geysermc.api.util.InputMode;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.BedrockActionTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerState;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerCommandPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

@Translator(packet = PlayerActionPacket.class)
public class BedrockActionInjector extends BedrockActionTranslator {
    @Override
    public void translate(GeyserSession session, PlayerActionPacket packet) {
        if (!GE.getConfig().isEnableToggleBlock()) {
            super.translate(session, packet);
        } else if (!packet.getAction().equals(PlayerActionType.START_SNEAK)
                && !packet.getAction().equals(PlayerActionType.STOP_SNEAK) && !(packet.getAction().equals(PlayerActionType.DROP_ITEM) && session.isSneaking())) {
            super.translate(session, packet);
        }
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        SessionPlayerEntity playerEntity = session.getPlayerEntity();
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
            case START_SPRINT -> {
                // Dont allow blocking if we start sprinting
                if (GE.getConfig().isEnableToggleBlock() && ShieldUtils.getBlocking(session) && ShieldUtils.disableBlocking(session)) {
                    playerEntity.updateBedrockMetadata();
                }
            }
            case START_SNEAK -> {
                if (GE.getConfig().isEnableToggleBlock()) {
                    ServerboundPlayerCommandPacket startSneakPacket = new ServerboundPlayerCommandPacket(playerEntity.getEntityId(), PlayerState.START_SNEAKING);
                    session.sendDownstreamGamePacket(startSneakPacket);


                    ShieldUtils.setSneaking(session, true);
                }
            }
            case STOP_SNEAK -> {
                if (GE.getConfig().isEnableToggleBlock()) {
                    ServerboundPlayerCommandPacket stopSneakPacket = new ServerboundPlayerCommandPacket(playerEntity.getEntityId(), PlayerState.STOP_SNEAKING);
                    session.sendDownstreamGamePacket(stopSneakPacket);

                    ShieldUtils.setSneaking(session, false);
                }
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

                if (GE.getConfig().isEnableToggleBlock() && ShieldUtils.disableBlocking(session)) {
                    playerEntity.updateBedrockMetadata();
                }

                if (session.inputMode().equals(InputMode.TOUCH)) {
                    player.swingArm();
                }
            }
            // handle it here anyway just in case lmao but it doesnt work here at least on 1.21
            case DROP_ITEM -> {
                if (session.isSneaking()) {
                    player.getPreferences().runAction(Remappable.SNEAK_DROP);
                    session.getInventoryTranslator().updateInventory(session, session.getPlayerInventory());
                }
            }
        }
    }
}
