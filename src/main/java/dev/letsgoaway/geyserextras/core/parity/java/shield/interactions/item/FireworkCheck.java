package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.item;

import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.Interaction;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.InteractionUtils;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Pose;

public class FireworkCheck implements Interaction {
    @Override
    public boolean check(GeyserSession session) {
        if (!session.getPlayerInventory().getItemInHand().asItem().equals(Items.FIREWORK_ROCKET)) {
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
