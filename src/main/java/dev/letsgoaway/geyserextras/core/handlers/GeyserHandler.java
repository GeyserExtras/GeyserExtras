package dev.letsgoaway.geyserextras.core.handlers;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.*;
import dev.letsgoaway.geyserextras.core.handlers.java.JavaSoundEntityInjector;
import dev.letsgoaway.geyserextras.core.handlers.java.JavaSoundInjector;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockBlockPickRequestTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSoundEntityPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSoundPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class GeyserHandler {
    public static void register() {
        SoundReplacer.loadSoundMappings();
        CommandHandler.loadCommands();
        registerUpstream();
        registerDownstream();
    }

    public static void registerUpstream() {
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundSoundPacket.class, new JavaSoundInjector());
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundSoundEntityPacket.class, new JavaSoundEntityInjector());
    }

    public static void registerDownstream() {
        if (ServerType.type.equals(ServerType.EXTENSION)) {
            Registries.BEDROCK_PACKET_TRANSLATORS.register(CommandRequestPacket.class, new BedrockCommandRequestInjector());
        }
        // Cooldown
        Registries.BEDROCK_PACKET_TRANSLATORS.register(PlayerActionPacket.class, new BedrockActionInjector());

        /* Action intercept related */
        // PICK_BLOCK
        Registries.BEDROCK_PACKET_TRANSLATORS.register(BlockPickRequestPacket.class, new BedrockBlockPickRequestInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(EntityPickRequestPacket.class, new BedrockEntityPickRequestInjector());
        // OPEN_INVENTORY
        Registries.BEDROCK_PACKET_TRANSLATORS.register(InteractPacket.class, new BedrockInteractInjector());

    }


    public static ExtrasPlayer getPlayer(GeyserSession session) {
        return GE.connections.get(session.xuid());
    }
}
