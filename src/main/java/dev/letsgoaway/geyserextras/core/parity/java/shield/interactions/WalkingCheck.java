package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions;

import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.session.GeyserSession;

public class WalkingCheck implements Interaction {
    @Override
    public boolean check(GeyserSession session, Item heldItem, GeyserItemStack heldItemStack, Block block) {
        if (session.isSprinting() && !session.isSneaking()) {
            return false;
        }
        return true;
    }
}
