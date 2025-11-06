package dev.letsgoaway.geyserextras.core.injectors.java;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundServerLinksPacket;

@Translator(packet = ClientboundServerLinksPacket.class)
public class JavaServerLinksInjector extends PacketTranslator<ClientboundServerLinksPacket> {
    @Override
    public void translate(GeyserSession session, ClientboundServerLinksPacket packet) {
        ExtrasPlayer player = ExtrasPlayer.get(session);
        player.getServerLinksData().setServerLinks(packet.getLinks());
    }
}
