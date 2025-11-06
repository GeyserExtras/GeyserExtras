package dev.letsgoaway.geyserextras.core.injectors.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.bedrock.packet.ServerboundDiagnosticsPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;

@Translator(packet = ServerboundDiagnosticsPacket.class)
public class BedrockDiagnosticsInjector extends PacketTranslator<ServerboundDiagnosticsPacket> {
    @Override
    public void translate(GeyserSession session, ServerboundDiagnosticsPacket packet) {
        ExtrasPlayer player = ExtrasPlayer.get(session);

        player.setDiagnostics(packet.clone());
        if (player.getFpsBossBar() != null) {

            player.getFpsBossBar().removeBossBar();
            player.getFpsBossBar().addBossBar();
            player.getFpsBossBar().updateTitle(Component.text(player.getBossBarText()));
        }
        //SERVER.log(String.valueOf(packet.getAvgFps()));
        //  GeyserHandler.getPlayer(session).sendActionbarTitle("FPS: " + packet.getAvgFps());
    }
}
