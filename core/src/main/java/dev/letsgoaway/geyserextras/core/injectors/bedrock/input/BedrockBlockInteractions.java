package dev.letsgoaway.geyserextras.core.injectors.bedrock.input;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.data.PlayerBlockActionData;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

public class BedrockBlockInteractions {
    public static void translate(GeyserSession session, List<PlayerBlockActionData> playerActions) {
        // Send book update before any player action
        session.getBookEditCache().checkForSend();

        for (PlayerBlockActionData blockActionData : playerActions) {
            handle(session, blockActionData);
        }
    }

    private static void handle(GeyserSession session, PlayerBlockActionData blockActionData) {
        PlayerActionType action = blockActionData.getAction();
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        switch (action) {
            case DROP_ITEM -> {
                if (session.isSneaking()) {
                    player.getPreferences().runAction(Remappable.SNEAK_DROP);
                    session.getInventoryTranslator().updateInventory(session, session.getPlayerInventory());
                }
            }
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

        }
    }
}
