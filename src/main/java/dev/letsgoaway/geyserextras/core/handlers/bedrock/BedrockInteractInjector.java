package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.menus.VRInventoryMenu;
import dev.letsgoaway.geyserextras.core.parity.java.shield.ShieldUtils;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.entity.player.BedrockInteractTranslator;

import java.util.concurrent.TimeUnit;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

@Translator(packet = InteractPacket.class)
public class BedrockInteractInjector extends BedrockInteractTranslator {
    @Override
    public void translate(GeyserSession session, InteractPacket packet) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        if (!packet.getAction().equals(InteractPacket.Action.OPEN_INVENTORY)) {
            if (GE.getConfig().isEnableCustomCooldown()) {
                // seems like this is handled properly in BedrockInventoryTransactionTranslator
                // but ill handle it here anyway
                if (packet.getAction().equals(InteractPacket.Action.DAMAGE)) {
                    player.getCooldownHandler().setDigTicks(-1);
                    player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                } else if (packet.getAction().equals(InteractPacket.Action.MOUSEOVER)) {
                    player.getCooldownHandler().setLastMouseoverID(packet.getRuntimeEntityId());
                }
            }
            super.translate(session, packet);
        } else {
            if (player.isVR() && player.getPreferences().isEnableDoubleClickForVRQuickMenu()) {
                // Double click
                if (player.getPreferences().getVrMenuDoubleClickMS() > System.currentTimeMillis() - player.getLastInventoryClickTime()) {
                    if (player.getVrInventoryMenuFuture() != null && !player.getVrInventoryMenuFuture().isCancelled() && !player.getVrInventoryMenuFuture().isDone()) {
                        player.getVrInventoryMenuFuture().cancel(false);
                        // open vr menu
                        player.sendForm(new VRInventoryMenu());
                    } else {
                        player.setLastInventoryClickTime(System.currentTimeMillis());
                        player.setVrInventoryMenuFuture(session.scheduleInEventLoop(() -> {
                            Action.OPEN_INVENTORY.run(player);
                        }, player.getPreferences().getVrMenuDoubleClickMS() + 20, TimeUnit.MILLISECONDS));
                    }
                    return;
                } else {
                    player.setLastInventoryClickTime(System.currentTimeMillis());
                    player.setVrInventoryMenuFuture(session.scheduleInEventLoop(() -> {
                        Action.OPEN_INVENTORY.run(player);
                    }, player.getPreferences().getVrMenuDoubleClickMS() + 20, TimeUnit.MILLISECONDS));
                    return;
                }
            }
            Remappable bind = player.getSession().isSneaking() ? Remappable.SNEAK_INVENTORY : Remappable.OPEN_INVENTORY;
            if (player.getPreferences().isDefault(bind) || player.getPreferences().getAction(bind).equals(Action.OPEN_INVENTORY)) {
                if (GE.getConfig().isEnableToggleBlock() && ShieldUtils.disableBlocking(session)) {
                    session.getPlayerEntity().updateBedrockMetadata();
                }
                super.translate(session, packet);
                return;
            }
            Entity entity;
            if (packet.getRuntimeEntityId() == session.getPlayerEntity().getGeyserId()) {
                //Player is not in entity cache
                entity = session.getPlayerEntity();
            } else {
                entity = session.getEntityCache().getEntityByGeyserId(packet.getRuntimeEntityId());
            }
            if (entity == null)
                return;
            player.getPreferences().getAction(bind).run(player);
        }
    }
}
