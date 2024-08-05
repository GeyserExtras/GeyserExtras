package dev.letsgoaway.geyserextras.core.handlers;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.BedrockActionInjector;
import dev.letsgoaway.geyserextras.core.handlers.java.JavaSoundInjector;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSoundPacket;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class GeyserHandler {
    public static void register() {
        registerDownstream();
        registerUpstream();
    }

    public static void registerUpstream() {
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundSoundPacket.class, new JavaSoundInjector());
    }

    public static void registerDownstream() {
        Registries.BEDROCK_PACKET_TRANSLATORS.register(PlayerActionPacket.class, new BedrockActionInjector());
    }


    public static ExtrasPlayer getPlayer(GeyserSession session) {
        return GE.connections.get(session.xuid());
    }
}
