package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.blockitem;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.Interaction;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

public class DirtToolCheck implements Interaction {
    private static List<Item> changesBlocks = List.of(
            Items.NETHERITE_SHOVEL,
            Items.DIAMOND_SHOVEL,
            Items.GOLDEN_SHOVEL,
            Items.IRON_SHOVEL,
            Items.STONE_SHOVEL,
            Items.WOODEN_SHOVEL,
            Items.BRUSH
    );

    private static List<Item> tillsBlocks = List.of(
            Items.NETHERITE_HOE,
            Items.DIAMOND_HOE,
            Items.GOLDEN_HOE,
            Items.IRON_HOE,
            Items.STONE_HOE,
            Items.WOODEN_HOE
    );
    private static List<Block> blocksThatChange = List.of(
            Blocks.DIRT,
            Blocks.COARSE_DIRT,
            Blocks.ROOTED_DIRT,
            Blocks.GRASS_BLOCK
    );


    @Override
    public boolean check(GeyserSession session) {
        if (GeyserHandler.getPlayer(session).getCooldownHandler().isLastClickWasAirClick()) {
            return true;
        }
        Item heldItem = session.getPlayerInventory().getItemInHand().asItem();
        Block block = session.getGeyser().getWorldManager().blockAt(session, session.getLastInteractionBlockPosition()).block();

        Block aboveBlock = session.getGeyser().getWorldManager().blockAt(session, session.getLastInteractionBlockPosition().up(1)).block();
        if (blocksThatChange.contains(block) && !(aboveBlock.equals(Blocks.AIR) || aboveBlock.equals(Blocks.CAVE_AIR) || aboveBlock.equals(Blocks.VOID_AIR))) {
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
        // one off exception block for hoes, they still get tilled
        if (block == Blocks.DIRT_PATH && tillsBlocks.contains(heldItem)) {
            GeyserHandler.getPlayer(session).swingArm();
            return false;
        }
        return true;
    }
}
