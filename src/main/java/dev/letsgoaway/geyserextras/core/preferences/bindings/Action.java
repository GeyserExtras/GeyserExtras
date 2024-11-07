package dev.letsgoaway.geyserextras.core.preferences.bindings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.menus.MainMenu;
import dev.letsgoaway.geyserextras.core.parity.java.menus.tablist.PlayerListMenu;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.entity.type.living.animal.horse.AbstractHorseEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.util.InventoryUtils;
import org.geysermc.mcprotocollib.protocol.data.game.ClientCommand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerState;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundClientCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerCommandPacket;

public enum Action {
    DEFAULT,
    DISABLED,
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
                        + player.translate("debug.prefix")
                        + " " + ChatColor.RESET
                        + player.translate("debug.advanced_tooltips." + onOrOff));
                session.getInventoryTranslator().updateInventory(session, session.getPlayerInventory());
            }
            case OPEN_GE_MENU -> player.sendForm(new MainMenu());
            case OPEN_ADVANCEMENTS -> session.getAdvancementsCache().buildAndShowMenuForm();
            case OPEN_STATISTICS -> {
                session.setWaitingForStatistics(true);
                ServerboundClientCommandPacket packet = new ServerboundClientCommandPacket(ClientCommand.STATS);
                session.sendDownstreamGamePacket(packet);
            }
            case OPEN_INVENTORY -> {
                // Taken from BedrockInteractTranslator
                if (session.getOpenInventory() == null) {
                    Entity ridingEntity = session.getPlayerEntity().getVehicle();
                    if (ridingEntity instanceof AbstractHorseEntity || (ridingEntity != null && ridingEntity.getDefinition().entityType().toString().contains("_CHEST_"))) {
                        // This mob has an inventory of its own that we should open instead.
                        ServerboundPlayerCommandPacket openVehicleWindowPacket = new ServerboundPlayerCommandPacket(session.getPlayerEntity().getEntityId(), PlayerState.OPEN_VEHICLE_INVENTORY);
                        session.sendDownstreamGamePacket(openVehicleWindowPacket);
                    } else {
                        InventoryUtils.openInventory(session, session.getPlayerInventory());
                    }
                }
            }
            case PLAYER_LIST -> {
              player.sendForm(new PlayerListMenu());
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

    public String translate(ExtrasPlayer player) {
        switch (this) {
            case DEFAULT -> {
                return BedrockLocale.GUI.DEFAULT;
            }
            case DISABLED -> {
                return BedrockLocale.OPTIONS.DISABLED;
            }
            case SWAP_OFFHAND -> {
                return player.translate("key.swapOffhand");
            }
            case RECONNECT -> {
                return player.translateGE("ge.reconnect");
            }
            case TOGGLE_TOOLTIPS -> {
                GeyserSession session = player.getSession();
                String onOrOff = session.isAdvancedTooltips() ? "on" : "off";
                return player.translate("debug.advanced_tooltips." + onOrOff);
            }
            case OPEN_GE_MENU -> {

            }
            case OPEN_ADVANCEMENTS -> {
                return player.translate("gui.advancements");
            }
            case OPEN_STATISTICS -> {
                return BedrockLocale.GUI.STATS;
            }
            case PLAYER_LIST -> {
                return BedrockLocale.KEY.PLAYER_LIST;
            }
            case PLATFORM_LIST -> {
            }
            case OPEN_INVENTORY -> {
                return BedrockLocale.KEY.OPEN_INVENTORY;
            }
            case CUSTOM -> {
                return BedrockLocale.GUI.CUSTOM;
            }
        }
        return this.name();
    }
}
