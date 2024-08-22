package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.core.ConfigLoader;
import dev.letsgoaway.geyserextras.core.parity.java.shield.ShieldUtils;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.java.inventory.JavaOpenScreenTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory.ClientboundOpenScreenPacket;

public class JavaOpenScreenInjector extends JavaOpenScreenTranslator {
    @Override
    public void translate(GeyserSession session, ClientboundOpenScreenPacket packet) {
        super.translate(session, packet);
        if (ConfigLoader.config.isEnableToggleBlock() && ShieldUtils.disableBlocking(session)) {
            session.getPlayerEntity().updateBedrockMetadata();
        }
    }
}
