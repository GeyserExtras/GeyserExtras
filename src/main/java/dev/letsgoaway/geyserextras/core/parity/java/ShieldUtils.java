package dev.letsgoaway.geyserextras.core.parity.java;

import dev.letsgoaway.geyserextras.ReflectionAPI;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.TickMath;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket;
import org.geysermc.geyser.entity.attribute.GeyserAttributeType;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.*;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Pose;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerAction;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.FoodProperties;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerActionPacket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShieldUtils {
    public static void checkBlock(GeyserSession session) {
        if (getBlocking(session)) {
            if (disableBlocking(session)) {
                session.getPlayerEntity().updateBedrockMetadata();
            }
        } else {
            if (attemptToBlock(session)) {
                session.getPlayerEntity().updateBedrockMetadata();
            }
        }
    }

    public static boolean getBlocking(GeyserSession session) {
        return session.getPlayerEntity().getFlag(EntityFlag.BLOCKING);
    }

    public static void updateBlockSpeed(GeyserSession session) {
        // Makes your camera zoom in on bedrock :/
         /*
                UpdateAttributesPacket attributesPacket = new UpdateAttributesPacket();
                attributesPacket.setRuntimeEntityId(session.getPlayerEntity().getGeyserId());
                attributesPacket.setAttributes(Collections.singletonList(
                        GeyserAttributeType.MOVEMENT_SPEED.getAttribute(0.10000000149011612f / 4)));
                session.sendUpstreamPacket(attributesPacket);
          */
    }

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
    public static final List<Item> dontBlock = List.of(
            Items.FIREWORK_ROCKET,
            Items.LINGERING_POTION,
            Items.BOW,
            Items.CROSSBOW,
            Items.ENDER_PEARL,
            Items.SNOWBALL,
            Items.ENDER_EYE,
            Items.SPLASH_POTION,
            Items.EXPERIENCE_BOTTLE,
            Items.WIND_CHARGE
    );

    private static boolean itemCheck(GeyserSession session) {
        GeyserItemStack mainHandItem = session.getPlayerInventory().getItemInHand();
        Item heldItem = mainHandItem.asItem();

        // Going to place block, spawn entity, equip currently held armor/elytra or summon boat,
        // and we should not block the shield if that is the case.
        if (heldItem instanceof BlockItem
                || heldItem instanceof SpawnEggItem
                || heldItem instanceof ArmorItem
                || heldItem instanceof ElytraItem
                || heldItem instanceof BoatItem) {
            return false;
        }

        return !dontBlock.contains(heldItem);
    }

    // If we can eat, prefer eating over shield blocking
    private static boolean foodCheck(GeyserSession session) {
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

    private static boolean canEat(GeyserSession session) {
        AttributeData hunger = session.getPlayerEntity().getAttributes().get(GeyserAttributeType.HUNGER);
        return session.getGameMode().equals(GameMode.CREATIVE) || hunger.getValue() != hunger.getMaximum();
    }


    public static boolean canBlock(GeyserSession session) {
        if (session.isSprinting() && !session.isSneaking()) {
            return false;
        }
        if (!itemCheck(session)) {
            return false;
        }
        if (!foodCheck(session)) {
            return false;
        }
        return true;
    }

    /**
     * Checks to see if a shield is in either hand to activate blocking. If so, it sets the Bedrock client to display
     * blocking and sends a packet to the Java server.
     */
    public static boolean attemptToBlock(GeyserSession session) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        if (session.isSprinting() && !session.isSneaking()) {
            return false;
        }
        if (!itemCheck(session)) {
            return false;
        }
        if (!foodCheck(session)) {
            return false;
        }

        if (session.getPlayerInventory().getItemInHand().asItem() == Items.SHIELD) {
            session.useItem(Hand.MAIN_HAND);
        } else if (session.getPlayerInventory().getOffhand().asItem() == Items.SHIELD) {
            session.useItem(Hand.OFF_HAND);
        } else {
            // No blocking
            return false;
        }

        session.getPlayerEntity().setFlag(EntityFlag.BLOCKING, true);

        // Metadata should be updated later
        return true;
    }

    public static boolean disableBlocking(GeyserSession session) {
        if (getBlocking(session)) {
            ServerboundPlayerActionPacket releaseItemPacket = new ServerboundPlayerActionPacket(PlayerAction.RELEASE_USE_ITEM,
                    Vector3i.ZERO, Direction.DOWN, 0);
            session.sendDownstreamGamePacket(releaseItemPacket);
            session.getPlayerEntity().setFlag(EntityFlag.BLOCKING, false);
            return true;
        }
        return false;
    }

    // Used to sneak without blocking shield
    public static void setSneaking(GeyserSession session, boolean sneak) {
        try {
            Method setSneaking = ReflectionAPI.getMethod(GeyserSession.class, "setSneaking", boolean.class);
            ReflectionAPI.invokeMethod(session, setSneaking, sneak);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
