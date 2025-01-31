package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.ServerboundDiagnosticsPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

@Translator(packet = ServerboundDiagnosticsPacket.class)
public class BedrockDiagnosticsInjector extends PacketTranslator<ServerboundDiagnosticsPacket> {
    @Override
    public void translate(GeyserSession session, ServerboundDiagnosticsPacket packet) {
        GeyserHandler.getPlayer(session).setDiagnostics(packet.clone());
        SERVER.log(String.valueOf(packet.getAvgFps()));
      //  GeyserHandler.getPlayer(session).sendActionbarTitle("FPS: " + packet.getAvgFps());
    }
}
