package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.menus.MainMenu;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraSetInstruction;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraTargetInstruction;
import org.cloudburstmc.protocol.bedrock.packet.CameraInstructionPacket;
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
                    player.hungerSprintCancel();
                } else if (packet.getAction().equals(InteractPacket.Action.MOUSEOVER)) {
                    player.getCooldownHandler().setLastMouseoverID(packet.getRuntimeEntityId());
                }
            }
            super.translate(session, packet);
        } else {
            if (player.getPreferences().isEnableDoubleClickShortcut() && !(player.getSession().isSneaking() && !player.getPreferences().isDefault(Remappable.SNEAK_INVENTORY))) {
                // Double click
                if (player.getPreferences().getDoubleClickMS() > System.currentTimeMillis() - player.getLastInventoryClickTime()) {
                    if (player.getDoubleClickShortcutFuture() != null && !player.getDoubleClickShortcutFuture().isCancelled() && !player.getDoubleClickShortcutFuture().isDone()) {
                        player.getDoubleClickShortcutFuture().cancel(false);
                        // open menu
                        player.sendForm(new MainMenu());
                    } else {
                        player.setLastInventoryClickTime(System.currentTimeMillis());
                        player.setDoubleClickShortcutFuture(session.scheduleInEventLoop(() -> {
                           player.getPreferences().runAction(Remappable.OPEN_INVENTORY);
                        }, player.getPreferences().getDoubleClickMS() + 20, TimeUnit.MILLISECONDS));
                    }
                    return;
                } else {
                    player.setLastInventoryClickTime(System.currentTimeMillis());
                    player.setDoubleClickShortcutFuture(session.scheduleInEventLoop(() -> {
                        player.getPreferences().runAction(Remappable.OPEN_INVENTORY);
                    }, player.getPreferences().getDoubleClickMS() + 20, TimeUnit.MILLISECONDS));
                    return;
                }
            }
            Remappable bind = player.getSession().isSneaking() ? Remappable.SNEAK_INVENTORY : Remappable.OPEN_INVENTORY;
            if (player.getPreferences().isDefault(bind) || player.getPreferences().getAction(bind).equals(Action.OPEN_INVENTORY)) {
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
