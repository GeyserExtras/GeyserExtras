package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket;
import org.geysermc.geyser.entity.attribute.GeyserAttributeType;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockInventoryTransactionTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.Attribute;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.AttributeType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerAction;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.FoodProperties;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerActionPacket;

import java.util.Collections;
import java.util.List;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;
// I love it when bedrock randomly sends data in random packets
// of which the name they have is completely irrelavant to what
// the data does it really helps me waste time trying to figure
// out why the damage event in BedrockInteractTranslator
// doesnt work :3

// but its ok we still win though

// YO ITS THE SAME WITH BLOCK DESTROY

// BUT WE STILL WIN THOUGH I THINK
@Translator(packet = InventoryTransactionPacket.class)
public class BedrockInventoryTransactionInjector extends BedrockInventoryTransactionTranslator {
    @Override
    public void translate(GeyserSession session, InventoryTransactionPacket packet) {
        //SERVER.log("INVENTORY TRANSACTION PACKET: " + packet.getTransactionType().name() + " " + packet.getActionType());
        super.translate(session, packet);
        // Entity Damage
        if (packet.getTransactionType().equals(InventoryTransactionType.ITEM_USE_ON_ENTITY)) {
            Entity entity = session.getEntityCache().getEntityByGeyserId(packet.getRuntimeEntityId());
            if (entity == null)
                return;

            if (packet.getActionType() == 1) {
                if (Config.toggleBlock) {
                    if (BedrockInventoryTransactionInjector.disableBlocking(session)) {
                        session.getPlayerEntity().updateBedrockMetadata();
                        session.getPlayerEntity().resetAttributes();
                    }
                }
                ExtrasPlayer player = GeyserHandler.getPlayer(session);
                player.getCooldownHandler().setDigTicks(-1);
                player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
            }
        }
        // Block Breaking
        if (packet.getTransactionType().equals(InventoryTransactionType.ITEM_USE)) {
            ExtrasPlayer player = GeyserHandler.getPlayer(session);
            if (Config.toggleBlock && packet.getActionType() == 1) {
                checkBlock(session);
            }

            if (packet.getActionType() == 2) {

                // Disable the GeyserExtras cooldown until next player action to
                // match java
                player.getCooldownHandler().setDigTicks(5);
            }
        }
    }

    // TODO: move shield stuff into seperate class
    public static void checkBlock(GeyserSession session) {
        if (!getBlocking(session)) {
            if (attemptToBlock(session)) {
                UpdateAttributesPacket attributesPacket = new UpdateAttributesPacket();
                attributesPacket.setRuntimeEntityId(session.getPlayerEntity().getGeyserId());
                attributesPacket.setAttributes(Collections.singletonList(
                        GeyserAttributeType.MOVEMENT_SPEED.getAttribute(0.10000000149011612f / 4)));
                session.sendUpstreamPacket(attributesPacket);
                session.getPlayerEntity().updateBedrockMetadata();
            }
        } else {
            if (disableBlocking(session)) {
                session.getPlayerEntity().updateBedrockMetadata();
                session.getPlayerEntity().resetAttributes();
            }
        }
    }

    public static boolean getBlocking(GeyserSession session) {
        return session.getPlayerEntity().getFlag(EntityFlag.BLOCKING);
    }

    public static void updateBlockSpeed(GeyserSession session) {
        if (getBlocking(session)) {
            UpdateAttributesPacket attributesPacket = new UpdateAttributesPacket();
            attributesPacket.setRuntimeEntityId(session.getPlayerEntity().getGeyserId());
            attributesPacket.setAttributes(List.of(
                    GeyserAttributeType.MOVEMENT_SPEED.getAttribute(0.10000000149011612f / 4)));
            session.sendUpstreamPacket(attributesPacket);
        } else {
            session.getPlayerEntity().updateBedrockMetadata();
        }
    }

    /**
     * Checks to see if a shield is in either hand to activate blocking. If so, it sets the Bedrock client to display
     * blocking and sends a packet to the Java server.
     */
    public static boolean attemptToBlock(GeyserSession session) {
        // If we can eat, prefer eating over shield blocking
        FoodProperties foodProperties = session.getPlayerInventory().getItemInHand().getComponent(DataComponentType.FOOD);
        if (foodProperties != null) {
            AttributeData hunger = session.getPlayerEntity().getAttributes().get(GeyserAttributeType.HUNGER);
            if (foodProperties.isCanAlwaysEat() || hunger.getValue() != hunger.getMaximum()) {
                return false;
            }
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

        if (session.getPlayerEntity().getFlag(EntityFlag.BLOCKING)) {
            ServerboundPlayerActionPacket releaseItemPacket = new ServerboundPlayerActionPacket(PlayerAction.RELEASE_USE_ITEM,
                    Vector3i.ZERO, Direction.DOWN, 0);
            session.sendDownstreamGamePacket(releaseItemPacket);
            session.getPlayerEntity().setFlag(EntityFlag.BLOCKING, false);
            return true;
        }
        return false;
    }
}
