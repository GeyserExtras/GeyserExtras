package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions;

import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.session.GeyserSession;

public interface Interaction {
    boolean check(GeyserSession session, Item heldItem, GeyserItemStack heldItemStack, Block block);

}
