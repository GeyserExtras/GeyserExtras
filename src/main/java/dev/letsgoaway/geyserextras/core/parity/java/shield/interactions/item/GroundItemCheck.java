package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.item;

import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.Interaction;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.InteractionUtils;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

// Items that can be placed/used on the ground should not block the shield.
public class GroundItemCheck implements Interaction {
    private static final List<Item> dontBlock = List.of(
            Items.DEBUG_STICK,
            Items.FLINT_AND_STEEL
    );

    @Override
    public boolean check(GeyserSession session, Item heldItem, GeyserItemStack heldItemStack, Block block) {
        if (InteractionUtils.isAirClick(session)) {
            return true;
        }
        return !dontBlock.contains(heldItem);
    }
}
