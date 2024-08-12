package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions;

import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.session.GeyserSession;

public class InteractionUtils {
    public static boolean isAirClick(GeyserSession session) {
        return GeyserHandler.getPlayer(session).getCooldownHandler().isLastClickWasAirClick();
    }
    public static boolean isAir(Block block) {
        return block.equals(Blocks.AIR) || block.equals(Blocks.CAVE_AIR) || block.equals(Blocks.VOID_AIR);
    }
}
