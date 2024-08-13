package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.text.MessageTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundTabListPacket;

// NOTE: If Geyser ever implements this, make sure to extend their translator!
public class JavaTabListInjector extends PacketTranslator<ClientboundTabListPacket> {
    @Override
    public void translate(GeyserSession session, ClientboundTabListPacket packet) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        player.getTabListData().setHeader(MessageTranslator.convertMessage(session, packet.getHeader()));
        player.getTabListData().setFooter(MessageTranslator.convertMessage(session, packet.getFooter()));
    }
}
