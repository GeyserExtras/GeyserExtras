package dev.letsgoaway.geyserextras.core.parity.java.shield;

import dev.letsgoaway.geyserextras.ReflectionAPI;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.*;
import dev.letsgoaway.geyserextras.core.parity.java.shield.interactions.blockitem.DirtToolCheck;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerAction;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerActionPacket;

import java.lang.reflect.Method;
import java.util.List;

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
        // TODO: make blocking reactivate when standing
/*
        if (session.getArmAnimationTicks() >= 0) {
            // As of 1.18.2 Java Edition, it appears that the swing time is dynamically updated depending on the
            // player's effect status, but the animation can cut short if the duration suddenly decreases
            // (from suddenly no longer having mining fatigue, for example)
            // This math is referenced from Java Edition 1.18.2
            int swingTotalDuration;
            int hasteLevel = Math.max(session.getEffectCache().getHaste(), session.getEffectCache().getConduitPower());
            if (hasteLevel > 0) {
                swingTotalDuration = 6 - hasteLevel;
            } else {
                int miningFatigueLevel = session.getEffectCache().getMiningFatigue();
                if (miningFatigueLevel > 0) {
                    swingTotalDuration = 6 + miningFatigueLevel * 2;
                } else {
                    swingTotalDuration = 6;
                }
            }
            if (session.getArmAnimationTicks() + 1 >= swingTotalDuration) {
                // Attempt to re-activate blocking as our swing animation is up
                if (attemptToBlock(session)) {
                    session.getPlayerEntity().updateBedrockMetadata();
                }
            }
        }

 */
    }

    private static final List<Interaction> interactionCheckList = List.of(
            new WalkingCheck(),
            new ItemCheck(),
            new FoodCheck(),
            new DirtToolCheck()
    );

    public static boolean canBlock(GeyserSession session) {
        for (Interaction interaction : interactionCheckList) {
            if (!interaction.check(session)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see if a shield is in either hand to activate blocking. If so, it sets the Bedrock client to display
     * blocking and sends a packet to the Java server.
     */
    public static boolean attemptToBlock(GeyserSession session) {
        if (!canBlock(session)) {
            GeyserHandler.getPlayer(session).getCooldownHandler().setLastClickWasAirClick(false);
            return false;
        }
        GeyserHandler.getPlayer(session).getCooldownHandler().setLastClickWasAirClick(false);

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
