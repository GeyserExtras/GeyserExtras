package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.item;

import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.Interaction;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.InteractionUtils;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Pose;

public class FireworkCheck implements Interaction {
    @Override
    public boolean check(GeyserSession session, Item heldItem, GeyserItemStack heldItemStack, Block block) {
        if (!heldItem.equals(Items.FIREWORK_ROCKET)) {
            return true;
        }
        // Using a firework when flying
        if (InteractionUtils.isAirClick(session) && session.getPose().equals(Pose.FALL_FLYING)) {
            return false;
        }
        // Using a firework on ground
        return InteractionUtils.isAirClick(session);
    }
}
