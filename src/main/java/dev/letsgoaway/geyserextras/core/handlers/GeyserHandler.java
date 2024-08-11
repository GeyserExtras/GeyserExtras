package dev.letsgoaway.geyserextras.core.handlers;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.*;
import dev.letsgoaway.geyserextras.core.handlers.java.*;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.*;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.*;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.*;

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
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundUpdateAttributesPacket.class, new JavaUpdateAttributesInjector());
    }

    public static void registerDownstream() {
        if (ServerType.type.equals(ServerType.EXTENSION)) {
            Registries.BEDROCK_PACKET_TRANSLATORS.register(CommandRequestPacket.class, new BedrockCommandRequestInjector());
        }
        // Cooldown
        Registries.BEDROCK_PACKET_TRANSLATORS.register(PlayerActionPacket.class, new BedrockActionInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(InventoryTransactionPacket.class, new BedrockInventoryTransactionInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(MobEquipmentPacket.class, new BedrockMobEquipmentInjector());

        // Emotes
        Registries.BEDROCK_PACKET_TRANSLATORS.register(EmoteListPacket.class, new BedrockEmoteListInjector());

        /* Action intercept related */
        // PICK_BLOCK
        Registries.BEDROCK_PACKET_TRANSLATORS.register(BlockPickRequestPacket.class, new BedrockBlockPickRequestInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(EntityPickRequestPacket.class, new BedrockEntityPickRequestInjector());
        // OPEN_INVENTORY and cooldown stuff
        Registries.BEDROCK_PACKET_TRANSLATORS.register(InteractPacket.class, new BedrockInteractInjector());
    }


    public static ExtrasPlayer getPlayer(GeyserSession session) {
        return GE.connections.get(session.xuid());
    }
}
