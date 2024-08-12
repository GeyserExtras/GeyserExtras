package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.item;

import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.Interaction;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.*;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

public class ItemCheck implements Interaction {
    private static final List<Item> dontBlock = List.of(
            Items.LINGERING_POTION,
            Items.BOW,
            Items.CROSSBOW,
            Items.TRIDENT,
            Items.ENDER_PEARL,
            Items.SNOWBALL,
            Items.ENDER_EYE,
            Items.SPLASH_POTION,
            Items.EXPERIENCE_BOTTLE,
            Items.WIND_CHARGE
    );

    public boolean check(GeyserSession session) {
        GeyserItemStack mainHandItem = session.getPlayerInventory().getItemInHand();
        Item heldItem = mainHandItem.asItem();
        boolean lastClickWasAir = GeyserHandler.getPlayer(session).getCooldownHandler().isLastClickWasAirClick();
        // Going to place block, spawn entity or summon boat,
        // and we should not block the shield if that is the case.
        if (heldItem instanceof BlockItem
                || heldItem instanceof SpawnEggItem
                || heldItem instanceof BoatItem
        ) {
            return lastClickWasAir;
        }
        // Going to equip currently held armor/elytra, not requiring that the player
        // is looking in the air or not.
        if (heldItem instanceof ArmorItem
                || heldItem instanceof ElytraItem) {
            return false;
        }

        return !dontBlock.contains(heldItem);
    }
}
