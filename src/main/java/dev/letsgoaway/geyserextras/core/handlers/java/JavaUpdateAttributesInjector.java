package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.entity.type.LivingEntity;
import org.geysermc.geyser.entity.type.player.PlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.java.entity.JavaUpdateAttributesTranslator;
import org.geysermc.geyser.util.AttributeUtils;
import org.geysermc.geyser.util.MathUtils;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.Attribute;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.AttributeType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundUpdateAttributesPacket;

@Translator(packet = ClientboundUpdateAttributesPacket.class)
public class JavaUpdateAttributesInjector extends JavaUpdateAttributesTranslator {
    @Override
    public void translate(GeyserSession session, ClientboundUpdateAttributesPacket packet) {
        if (packet.getEntityId() == session.getPlayerEntity().getEntityId()) {
            for (Attribute attribute : packet.getAttributes()) {
                if (attribute.getType().equals(AttributeType.Builtin.GENERIC_ATTACK_SPEED)) {
                    ExtrasPlayer player = GeyserHandler.getPlayer(session);
                    player.getCooldownHandler().setAttackSpeed(AttributeUtils.calculateValue(attribute));
                    // player.getCooldownHandler().setLastSwingTime(System.currentTimeMillis());
                    break;
                }
            }
            super.translate(session, packet);
            // disable geysers cooldown code for ours
            // 194 = lga = LetsGoAway
            // if for some reason you want to check
            // if my plugin is changing the attack speed
            // check this number
            session.setAttackSpeed(9999.194);
        } else {
            super.translate(session, packet);
        }
    }
}
