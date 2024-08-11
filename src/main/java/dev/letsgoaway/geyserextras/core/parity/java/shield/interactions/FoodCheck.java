package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions;

import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.geysermc.geyser.entity.attribute.GeyserAttributeType;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.FoodProperties;

import java.util.List;

public class FoodCheck implements Interaction {
    private static final List<Item> foods = List.of(
            Items.APPLE,
            Items.GOLDEN_APPLE,
            Items.ENCHANTED_GOLDEN_APPLE,
            Items.MELON_SLICE,
            Items.SWEET_BERRIES,
            Items.GLOW_BERRIES,
            Items.CHORUS_FRUIT,
            Items.CARROT,
            Items.GOLDEN_CARROT,
            Items.POTATO,
            Items.BAKED_POTATO,
            Items.POISONOUS_POTATO,
            Items.BEETROOT,
            Items.DRIED_KELP,
            Items.BEEF,
            Items.COOKED_BEEF,
            Items.PORKCHOP,
            Items.COOKED_PORKCHOP,
            Items.MUTTON,
            Items.COOKED_MUTTON,
            Items.CHICKEN,
            Items.COOKED_CHICKEN,
            Items.RABBIT,
            Items.COOKED_RABBIT,
            Items.COD,
            Items.COOKED_COD,
            Items.SALMON,
            Items.COOKED_SALMON,
            Items.TROPICAL_FISH,
            Items.PUFFERFISH,
            Items.BREAD,
            Items.COOKIE,
            Items.CAKE,
            Items.PUMPKIN_PIE,
            Items.ROTTEN_FLESH,
            Items.SPIDER_EYE,
            Items.MUSHROOM_STEW,
            Items.BEETROOT_SOUP,
            Items.RABBIT_STEW,
            Items.SUSPICIOUS_STEW,
            Items.MILK_BUCKET,
            Items.HONEY_BOTTLE,
            Items.OMINOUS_BOTTLE,
            Items.POTION
    );

    private static final List<Item> canAlwaysEatFoods = List.of(
            // For whatever reason regular
            // apples are still edible
            // on bedrock on any difficulty
            Items.APPLE,
            Items.GOLDEN_APPLE,
            Items.ENCHANTED_GOLDEN_APPLE,
            Items.CHORUS_FRUIT,
            Items.HONEY_BOTTLE,
            Items.OMINOUS_BOTTLE,
            Items.POTION,
            Items.MILK_BUCKET
    );

    @Override
    public boolean check(GeyserSession session) {
        // If we can eat, prefer eating over shield blocking
        GeyserItemStack mainHandItem = session.getPlayerInventory().getItemInHand();
        Item heldItem = mainHandItem.asItem();

        if (canAlwaysEatFoods.contains(heldItem)) {
            return false;
        }
        if (foods.contains(heldItem) && canEat(session)) {
            return false;
        }
        FoodProperties foodProperties = mainHandItem.getComponent(DataComponentType.FOOD);
        if (foodProperties != null) {
            if (foodProperties.isCanAlwaysEat() || canEat(session)) {
                return false;
            }
        }
        return true;
    }
    private boolean canEat(GeyserSession session) {
        AttributeData hunger = session.getPlayerEntity().getAttributes().get(GeyserAttributeType.HUNGER);
        return session.getGameMode().equals(GameMode.CREATIVE) || hunger.getValue() != hunger.getMaximum();
    }

}
