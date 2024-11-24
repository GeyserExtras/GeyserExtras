package dev.letsgoaway.geyserextras.core.handlers.java;

import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.java.inventory.JavaOpenScreenTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory.ClientboundOpenScreenPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class JavaOpenScreenInjector extends JavaOpenScreenTranslator {
    @Override
    public void translate(GeyserSession session, ClientboundOpenScreenPacket packet) {
        super.translate(session, packet);
    }
}
