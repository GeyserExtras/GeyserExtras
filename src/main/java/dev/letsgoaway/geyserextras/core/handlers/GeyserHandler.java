package dev.letsgoaway.geyserextras.core.handlers;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.BedrockActionInjector;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.BedrockBlockPickRequestInjector;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.BedrockEntityPickRequestInjector;
import dev.letsgoaway.geyserextras.core.handlers.java.JavaSoundEntityInjector;
import dev.letsgoaway.geyserextras.core.handlers.java.JavaSoundInjector;
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityPickRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockBlockPickRequestTranslator;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSoundEntityPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSoundPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class GeyserHandler {
    public static void register() {
        SoundReplacer.loadSoundMappings();
        registerUpstream();
        registerDownstream();
    }

    public static void registerUpstream() {
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundSoundPacket.class, new JavaSoundInjector());
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundSoundEntityPacket.class, new JavaSoundEntityInjector());
    }

    public static void registerDownstream() {
        Registries.BEDROCK_PACKET_TRANSLATORS.register(PlayerActionPacket.class, new BedrockActionInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(BlockPickRequestPacket.class, new BedrockBlockPickRequestInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(EntityPickRequestPacket.class, new BedrockEntityPickRequestInjector());
    }


    public static ExtrasPlayer getPlayer(GeyserSession session) {
        return GE.connections.get(session.xuid());
    }
}
