package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.item;

import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.Interaction;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.InteractionUtils;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

public class DirtToolCheck implements Interaction {
    private static final List<Item> changesBlocks = List.of(
            Items.NETHERITE_SHOVEL,
            Items.DIAMOND_SHOVEL,
            Items.GOLDEN_SHOVEL,
            Items.IRON_SHOVEL,
            Items.STONE_SHOVEL,
            Items.WOODEN_SHOVEL,
            Items.BRUSH
    );

    private static final List<Item> tillsBlocks = List.of(
            Items.NETHERITE_HOE,
            Items.DIAMOND_HOE,
            Items.GOLDEN_HOE,
            Items.IRON_HOE,
            Items.STONE_HOE,
            Items.WOODEN_HOE
    );
    private static final List<Block> blocksThatChange = List.of(
            Blocks.DIRT,
            Blocks.COARSE_DIRT,
            Blocks.ROOTED_DIRT,
            Blocks.GRASS_BLOCK
    );


    @Override
    public boolean check(GeyserSession session, Item heldItem, GeyserItemStack heldItemStack, Block block) {
        if (InteractionUtils.isAirClick(session)) {
            return true;
        }

        Block aboveBlock = session.getGeyser().getWorldManager().blockAt(session, session.getLastInteractionBlockPosition().up(1)).block();
        if (blocksThatChange.contains(block) && !(InteractionUtils.isAir(aboveBlock))) {
            // We can block because this interaction wont do anything.
            return true;
        }
        if (blocksThatChange.contains(block) && changesBlocks.contains(heldItem)) {
            return false;
        }
        if (blocksThatChange.contains(block) && tillsBlocks.contains(heldItem)) {
            // Arm doesnt swing for some reason when this happens
            GeyserHandler.getPlayer(session).swingArm();
            return false;
        }
        // one off exception block for hoes, paths still get tilled
        if (block == Blocks.DIRT_PATH && tillsBlocks.contains(heldItem)) {
            GeyserHandler.getPlayer(session).swingArm();
            return false;
        }
        return true;
    }
}
