package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.text.MessageTranslator;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundDisconnectPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
@Translator(packet = ClientboundDisconnectPacket.class)
public class JavaDisconnectInjector extends PacketTranslator<ClientboundDisconnectPacket> {
    @Override
    public void translate(GeyserSession session, ClientboundDisconnectPacket packet) {
        if (GE.getConfig().isAutoReconnect()){
            ExtrasPlayer player = GeyserHandler.getPlayer(session);
            if (ServerType.canRunTabList()) {
                player.sendToast("", MessageTranslator.convertMessage(packet.getReason(), session.locale()));
            }
            player.reconnect();
        }
    }
}
