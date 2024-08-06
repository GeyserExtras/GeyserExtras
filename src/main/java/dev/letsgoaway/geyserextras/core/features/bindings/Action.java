package dev.letsgoaway.geyserextras.core.features.bindings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.handlers.CommandHandler;
import dev.letsgoaway.geyserextras.core.handlers.bedrock.BedrockBlockPickRequestInjector;
import dev.letsgoaway.geyserextras.core.menus.MainMenu;
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.entity.type.living.animal.horse.AbstractHorseEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.InventoryUtils;
import org.geysermc.mcprotocollib.protocol.data.game.ClientCommand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerState;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundClientCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerCommandPacket;

public enum Action {
    DEFAULT,
    NOTHING,
    SWAP_OFFHAND,
    RECONNECT,
    TOGGLE_TOOLTIPS,
    OPEN_GE_MENU,
    OPEN_ADVANCEMENTS,
    OPEN_STATISTICS,
    PLAYER_LIST,
    PLATFORM_LIST,
    OPEN_INVENTORY,
    CUSTOM;


    public void run(ExtrasPlayer player) {
        GeyserSession session = player.getSession();
        switch (this) {
            case SWAP_OFFHAND -> session.requestOffhandSwap();
            case RECONNECT -> player.reconnect();
            case TOGGLE_TOOLTIPS -> {
                session.setAdvancedTooltips(!session.isAdvancedTooltips());
                String onOrOff = session.isAdvancedTooltips() ? "on" : "off";
                session.sendMessage(ChatColor.BOLD + ChatColor.YELLOW
                        + MinecraftLocale.getLocaleString("debug.prefix", session.locale())
                        + " " + ChatColor.RESET
                        + MinecraftLocale.getLocaleString("debug.advanced_tooltips." + onOrOff, session.locale()));
                session.getInventoryTranslator().updateInventory(session, session.getPlayerInventory());
            }
            case OPEN_GE_MENU -> {
                player.sendForm(new MainMenu());
            }
            case OPEN_ADVANCEMENTS -> {
                session.getAdvancementsCache().buildAndShowMenuForm();
            }
            case OPEN_STATISTICS -> {
                session.setWaitingForStatistics(true);
                ServerboundClientCommandPacket packet = new ServerboundClientCommandPacket(ClientCommand.STATS);
                session.sendDownstreamGamePacket(packet);
            }
            case OPEN_INVENTORY -> {
                // Taken from BedrockInteractTranslator
                if (session.getOpenInventory() == null) {
                    Entity ridingEntity = session.getPlayerEntity().getVehicle();
                    if (ridingEntity instanceof AbstractHorseEntity || (ridingEntity != null && ridingEntity.getDefinition().entityType() == EntityType.CHEST_BOAT)) {
                        // This mob has an inventory of its own that we should open instead.
                        ServerboundPlayerCommandPacket openVehicleWindowPacket = new ServerboundPlayerCommandPacket(session.getPlayerEntity().getEntityId(), PlayerState.OPEN_VEHICLE_INVENTORY);
                        session.sendDownstreamGamePacket(openVehicleWindowPacket);
                    } else {
                        InventoryUtils.openInventory(session, session.getPlayerInventory());
                    }
                }
            }
            case PLAYER_LIST -> {
                // TODO: playerlist
            }
            case PLATFORM_LIST -> {
                // TODO: platformlist
            }
            case CUSTOM -> {
                // TODO: custom
            }
            default -> {
            }
        }
    }

    public String getLocaleString(ExtrasPlayer player) {
        GeyserSession session = player.getSession();
        String locale = session.locale();
        switch (this) {
            case SWAP_OFFHAND -> {
                return MinecraftLocale.getLocaleString("key.swapOffhand", locale);
            }
            case TOGGLE_TOOLTIPS -> {
                String onOrOff = session.isAdvancedTooltips() ? "on" : "off";
                return MinecraftLocale.getLocaleString("debug.advanced_tooltips." + onOrOff, locale);
            }
            case OPEN_ADVANCEMENTS -> {
                return MinecraftLocale.getLocaleString("gui.advancements", locale);
            }
            case OPEN_STATISTICS -> {
                return MinecraftLocale.getLocaleString("gui.stats", locale);
            }
            case PLAYER_LIST -> {
                return MinecraftLocale.getLocaleString("key.playerlist", locale);
            }
            case DEFAULT -> {
                return MinecraftLocale.getLocaleString("options.gamma.default", locale);
            }
            case NOTHING -> {
                return MinecraftLocale.getLocaleString("addServer.resourcePack.disabled", locale);
            }
            case OPEN_INVENTORY -> {
                return MinecraftLocale.getLocaleString("tutorial.open_inventory.title", locale);
            }
            default -> {
                return this.name();
            }
        }
    }
}
