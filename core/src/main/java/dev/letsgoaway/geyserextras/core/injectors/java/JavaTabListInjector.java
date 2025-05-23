package dev.letsgoaway.geyserextras.core.injectors.java;

import dev.letsgoaway.geyserextras.core.utils.ReflectionAPI;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.text.MessageTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundTabListPacket;

// NOTE: If Geyser ever implements this, make sure to extend their translator!
@Translator(packet = ClientboundTabListPacket.class)
public class JavaTabListInjector extends PacketTranslator<ClientboundTabListPacket> {
    @Override
    public void translate(GeyserSession session, ClientboundTabListPacket packet) {
        // Causes
        //java.lang.NoSuchMethodError:
        // 'net.kyori.adventure.text.Component
        // org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundTabListPacket.getHeader()'
        // due to package errors
        if (!IsAvailable.adventure() || ReflectionAPI.getMethodOrNull(ClientboundTabListPacket.class, "getHeader") == null) {
            return;
        }
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        player.getTabListData().setHeader(MessageTranslator.convertMessage(session, packet.getHeader()));
        player.getTabListData().setFooter(MessageTranslator.convertMessage(session, packet.getFooter()));
    }
}
