package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import org.cloudburstmc.protocol.bedrock.packet.ServerboundDiagnosticsPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;

public class BedrockDiagnosticsInjector extends PacketTranslator<ServerboundDiagnosticsPacket> {
    @Override
    public void translate(GeyserSession session, ServerboundDiagnosticsPacket packet) {

    }
}
