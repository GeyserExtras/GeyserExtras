package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.item;

import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.Interaction;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.level.physics.PistonBehavior;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

import static org.geysermc.geyser.level.block.property.Properties.STAGE;
import static org.geysermc.geyser.level.block.type.Block.builder;


public class BonemealCheck implements Interaction {
    // In order from https://minecraft.wiki/w/Bone_Meal#Fertilizer

    // TODO: underwater seagrass interactions from https://minecraft.wiki/w/Bone_Meal#Creating_new_plants
    private static final List<Block> growableBlocks = List.of(
            Blocks.WHEAT,
            Blocks.CARROTS,
            Blocks.POTATOES,
            Blocks.BEETROOTS,
            Blocks.BAMBOO,
            Blocks.MELON_STEM,
            Blocks.PUMPKIN_STEM,
            Blocks.OAK_SAPLING,
            Blocks.SPRUCE_SAPLING,
            Blocks.BIRCH_SAPLING,
            Blocks.JUNGLE_SAPLING,
            Blocks.ACACIA_SAPLING,
            Blocks.CHERRY_SAPLING,
            Blocks.DARK_OAK_SAPLING,
            Blocks.AZALEA,
            Blocks.FLOWERING_AZALEA,
            Blocks.MANGROVE_PROPAGULE,
            Blocks.SUNFLOWER,
            Blocks.LILAC,
            Blocks.ROSE_BUSH,
            Blocks.PEONY,
            Blocks.SHORT_GRASS,
            Blocks.FERN,
            Blocks.SEAGRASS,
            Blocks.RED_MUSHROOM,
            Blocks.BROWN_MUSHROOM,
            Blocks.COCOA,
            Blocks.SWEET_BERRY_BUSH,
            Blocks.SEA_PICKLE,
            // Sugar cane cant be grown on Java
            // but bedrock will still try to interact with it
            // anyway
            Blocks.SUGAR_CANE,
            Blocks.KELP,
            Blocks.KELP_PLANT,
            // Same issue with flowers
            Blocks.ALLIUM,
            Blocks.AZURE_BLUET,
            Blocks.BLUE_ORCHID,
            Blocks.CORNFLOWER,
            Blocks.DANDELION,
            Blocks.LILY_OF_THE_VALLEY,
            Blocks.OXEYE_DAISY,
            Blocks.POPPY,
            Blocks.ORANGE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.RED_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.CRIMSON_FUNGUS,
            Blocks.WARPED_FUNGUS,
            Blocks.WEEPING_VINES,
            Blocks.WEEPING_VINES_PLANT,
            Blocks.TWISTING_VINES,
            Blocks.TWISTING_VINES_PLANT,
            Blocks.CAVE_VINES,
            Blocks.CAVE_VINES_PLANT,
            Blocks.GLOW_LICHEN,
            Blocks.MOSS_BLOCK,
            Blocks.BIG_DRIPLEAF,
            Blocks.BIG_DRIPLEAF_STEM,
            Blocks.SMALL_DRIPLEAF,
            Blocks.ROOTED_DIRT,
            Blocks.MANGROVE_LEAVES,
            Blocks.PINK_PETALS,
            Blocks.TORCHFLOWER_CROP,
            Blocks.PITCHER_CROP,
            Blocks.GRASS_BLOCK,
            Blocks.CRIMSON_NYLIUM,
            Blocks.WARPED_NYLIUM
    );

    @Override
    public boolean check(GeyserSession session, Item heldItem, GeyserItemStack heldItemStack, Block block) {
        if (heldItem.equals(Items.BONE_MEAL)) {
            return !growableBlocks.contains(block);
        }
        return true;
    }
}
