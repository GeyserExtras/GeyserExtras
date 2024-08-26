package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerId;
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket;
import org.geysermc.geyser.inventory.GeyserItemStack;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockMobEquipmentTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundSetCarriedItemPacket;

import java.util.concurrent.TimeUnit;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

@Translator(packet = MobEquipmentPacket.class)
public class BedrockMobEquipmentInjector extends BedrockMobEquipmentTranslator {
    @Override
    public void translate(GeyserSession session, MobEquipmentPacket packet) {
        if (GE.getConfig().isEnableCustomCooldown()) {
            ExtrasPlayer player = GeyserHandler.getPlayer(session);

            int newSlot = packet.getHotbarSlot();
            if (!session.isSpawned() || newSlot > 8 || packet.getContainerId() != ContainerId.INVENTORY
                    || session.getPlayerInventory().getHeldItemSlot() == newSlot) {
                // For the last condition - Don't update the slot if the slot is the same - not Java Edition behavior and messes with plugins such as Grief Prevention
                return;
            }

            // Send book update before switching hotbar slot
            session.getBookEditCache().checkForSend();

            GeyserItemStack oldItem = session.getPlayerInventory().getItemInHand();
            session.getPlayerInventory().setHeldItemSlot(newSlot);

            ServerboundSetCarriedItemPacket setCarriedItemPacket = new ServerboundSetCarriedItemPacket(newSlot);
            session.sendDownstreamGamePacket(setCarriedItemPacket);

            GeyserItemStack newItem = session.getPlayerInventory().getItemInHand();

            if (session.isSneaking() && newItem.asItem() == Items.SHIELD) {
                // Activate shield since we are already sneaking
                // (No need to send a release item packet - Java doesn't do this when swapping items)
                // Required to do it a tick later or else it doesn't register
                session.scheduleInEventLoop(() -> session.useItem(Hand.MAIN_HAND),
                        50, TimeUnit.MILLISECONDS);
            }

            if (oldItem.getJavaId() != newItem.getJavaId()) {
                // Java sends a cooldown indicator whenever you switch to a new item type
                player.getCooldownHandler().setDigTicks(-1);
                if (newItem.asItem().equals(Items.AIR)) {
                    // Still update cooldown, but dont space hotbar
                    player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                } else {
                    player.getCooldownHandler().setLastHotbarTime(System.currentTimeMillis());
                }
            }

            // Update the interactive tag, if an entity is present
            if (session.getMouseoverEntity() != null) {
                session.getMouseoverEntity().updateInteractiveTag();
            }
        } else {
            super.translate(session, packet);
        }
    }
}
