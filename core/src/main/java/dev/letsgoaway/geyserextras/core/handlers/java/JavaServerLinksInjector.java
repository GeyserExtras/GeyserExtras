package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundServerLinksPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoUpdatePacket;

import java.util.ArrayList;

@Translator(packet = ClientboundServerLinksPacket.class)
public class JavaServerLinksInjector extends PacketTranslator<ClientboundServerLinksPacket> {
    @Override
    public void translate(GeyserSession session, ClientboundServerLinksPacket packet) {
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        player.getServerLinksData().setServerLinks(packet.getLinks());
    }
}
