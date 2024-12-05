package dev.letsgoaway.geyserextras.core.handlers;

import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.util.PlatformType;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.*;
import dev.letsgoaway.geyserextras.core.handlers.java.*;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundServerLinksPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.*;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.*;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory.ClientboundOpenScreenPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.*;

import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class GeyserHandler {
    public static void register() {
        SoundReplacer.loadSoundMappings();
        CommandHandler.loadCommands();
        registerUpstream();
        registerDownstream();
    }

    public static void registerUpstream() {
        // Combat sounds
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundSoundPacket.class, new JavaSoundInjector());
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundSoundEntityPacket.class, new JavaSoundEntityInjector());
        // Shield stuff
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundEntityEventPacket.class, new JavaEntityEventInjector());
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundOpenScreenPacket.class, new JavaOpenScreenInjector());
        // Cooldown
        Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundUpdateAttributesPacket.class, new JavaUpdateAttributesInjector());

        // Tab list

        PlatformType platformType = GeyserImpl.getInstance().getPlatformType();

        // we do this for now because of adventure library issues (this sucks!!!)
        // todo: figure out a workaround or a better way to fix this
        if (ServerType.canRunTabList()) {
            Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundTabListPacket.class, new JavaTabListInjector());
            Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundPlayerInfoUpdatePacket.class, new JavaPlayerInfoUpdateInjector());
            // Server Links (1.21+)
            Registries.JAVA_PACKET_TRANSLATORS.register(ClientboundServerLinksPacket.class, new JavaServerLinksInjector());
        }
    }

    public static void registerDownstream() {
        if (ServerType.isExtension()) {
            Registries.BEDROCK_PACKET_TRANSLATORS.register(CommandRequestPacket.class, new BedrockCommandRequestInjector());
        }
        // Cooldown
        Registries.BEDROCK_PACKET_TRANSLATORS.register(PlayerAuthInputPacket.class, new BedrockPlayerAuthInputInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(InventoryTransactionPacket.class, new BedrockInventoryTransactionInjector());
        Registries.BEDROCK_PACKET_TRANSLATORS.register(MobEquipmentPacket.class, new BedrockMobEquipmentInjector());

        // Emotes
        Registries.BEDROCK_PACKET_TRANSLATORS.register(EmoteListPacket.class, new BedrockEmoteListInjector());

        // Settings
        Registries.BEDROCK_PACKET_TRANSLATORS.register(ServerSettingsRequestPacket.class, new BedrockServerSettingsRequestInjector());

        // FPS Counter
        // todo: why isnt this registering even though its being sent AAAAAAAAAAAAAAAAAAAAAAAAAAA
        Registries.BEDROCK_PACKET_TRANSLATORS.register(ServerboundDiagnosticsPacket.class, new BedrockDiagnosticsInjector());

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

    public static ExtrasPlayer getPlayer(UUID javaUUID) {
        for (ExtrasPlayer player : GE.connections.values()) {
            if (player.getJavaUUID().equals(javaUUID)) {
                return player;
            }
        }
        return null;
    }
}
