package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.handlers.CommandHandler;
import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import dev.letsgoaway.geyserextras.core.menus.MainMenu;
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockCommandRequestTranslator;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

@Translator(packet = CommandRequestPacket.class)
public class BedrockCommandRequestInjector extends BedrockCommandRequestTranslator {
    @Override
    public void translate(GeyserSession session, CommandRequestPacket packet) {
        if (CommandHandler.isExtrasCommand(packet.getCommand())) {
            ExtrasPlayer player = GeyserHandler.getPlayer(session);
            CommandHandler.runFromInput(player, packet.getCommand());
        } else {
            super.translate(session, packet);
        }
    }
}
