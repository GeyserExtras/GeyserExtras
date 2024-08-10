package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;

import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.geyser.level.block.type.SkullBlock;
import org.geysermc.geyser.registry.BlockRegistries;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.SkullCache;
import org.geysermc.geyser.translator.inventory.InventoryTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockInventoryTransactionTranslator;
import dev.letsgoaway.geyserextras.core.parity.java.ShieldUtils;

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
        ExtrasPlayer player = GeyserHandler.getPlayer(session);

        SERVER.log("INVENTORY TRANSACTION PACKET: " + packet.getTransactionType().name() + " " + packet.getActionType() + " " + (packet.getBlockPosition() == null ? "null" : packet.getBlockPosition().toString()) + " " + packet.getClickPosition().toString());
        SERVER.log(String.valueOf(System.currentTimeMillis() - player.getCooldownHandler().getLastBlockRightClickTime()));
        InventoryTransactionType type = packet.getTransactionType();
        // Trying to do a block interaction, but we should disable the shield first
        if (Config.toggleBlock && ShieldUtils.getBlocking(session)
                && (type.equals(InventoryTransactionType.ITEM_USE)
                || type.equals(InventoryTransactionType.ITEM_USE_ON_ENTITY))
                && packet.getActionType() == 0) {
            if (System.currentTimeMillis() - player.getCooldownHandler().getLastBlockRightClickTime() > 100 && ShieldUtils.disableBlocking(session)) {
                Vector3i position = packet.getBlockPosition();
                if (position == null) {
                    position = packet.getClickPosition().toInt();
                }
                restoreCorrectBlock(session, position, packet);
                session.getPlayerEntity().updateBedrockMetadata();
                // If the user can block again, then their item they are holding
                // does not have an important action
                // if it does, we should process it
                if (ShieldUtils.canBlock(session)) {
                    return;
                }
            } else {
                player.getCooldownHandler().setSkipNextItemUse1(true);
            }
            player.getCooldownHandler().setLastBlockRightClickTime(System.currentTimeMillis());
        }
        super.translate(session, packet);

        if (type.equals(InventoryTransactionType.ITEM_USE_ON_ENTITY)) {
            Entity entity = session.getEntityCache().getEntityByGeyserId(packet.getRuntimeEntityId());
            if (entity == null) return;

            // Entity Damage
            if (packet.getActionType() == 1) {
                if (Config.toggleBlock) {
                    if (ShieldUtils.disableBlocking(session)) {
                        session.getPlayerEntity().updateBedrockMetadata();
                    }
                }
                player.getCooldownHandler().setDigTicks(-1);
                player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
            }
        }
        if (type.equals(InventoryTransactionType.ITEM_USE)) {
            // Item use
            if (Config.toggleBlock) {
                if (packet.getActionType() == 0) {
                    player.getCooldownHandler().setLastBlockRightClickTime(System.currentTimeMillis());
                }
                if (packet.getActionType() == 1) {
                    if (player.getCooldownHandler().isSkipNextItemUse1()) {
                        player.getCooldownHandler().setSkipNextItemUse1(false);
                        return;
                    }
                    if (!session.isSneaking()) {
                        ShieldUtils.checkBlock(session);
                    } else if (System.currentTimeMillis() - player.getCooldownHandler().getLastBlockRightClickTime() > 100) {
                        ShieldUtils.checkBlock(session);
                    }
                }
            }
            if (packet.getActionType() == 2) { // Block Breaking
                // Disable the GeyserExtras cooldown until next player action to
                // match java
                player.getCooldownHandler().setDigTicks(5);
            }
        }
    }

    /**
     * Restore the correct block state from the server without updating the chunk cache.
     *
     * @param session  the session of the Bedrock client
     * @param blockPos the block position to restore
     */
    private void restoreCorrectBlock(GeyserSession session, Vector3i blockPos, InventoryTransactionPacket packet) {
        BlockState javaBlockState = session.getGeyser().getWorldManager().blockAt(session, blockPos);
        BlockDefinition bedrockBlock = session.getBlockMappings().getBedrockBlock(javaBlockState);

        if (javaBlockState.block() instanceof SkullBlock skullBlock && skullBlock.skullType() == SkullBlock.Type.PLAYER) {
            // The changed block was a player skull so check if a custom block was defined for this skull
            SkullCache.Skull skull = session.getSkullCache().getSkulls().get(blockPos);
            if (skull != null && skull.getBlockDefinition() != null) {
                bedrockBlock = skull.getBlockDefinition();
            }
        }

        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setDataLayer(0);
        updateBlockPacket.setBlockPosition(blockPos);
        updateBlockPacket.setDefinition(bedrockBlock);
        updateBlockPacket.getFlags().addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY);
        session.sendUpstreamPacket(updateBlockPacket);

        UpdateBlockPacket updateWaterPacket = new UpdateBlockPacket();
        updateWaterPacket.setDataLayer(1);
        updateWaterPacket.setBlockPosition(blockPos);
        updateWaterPacket.setDefinition(BlockRegistries.WATERLOGGED.get().get(javaBlockState.javaId()) ? session.getBlockMappings().getBedrockWater() : session.getBlockMappings().getBedrockAir());
        updateWaterPacket.getFlags().addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY);
        session.sendUpstreamPacket(updateWaterPacket);

        // Reset the item in hand to prevent "missing" blocks
        InventoryTranslator.PLAYER_INVENTORY_TRANSLATOR.updateSlot(session, session.getPlayerInventory(), session.getPlayerInventory().getOffsetForHotbar(packet.getHotbarSlot()));
    }

}
