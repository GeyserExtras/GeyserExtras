package dev.letsgoaway.geyserextras.core.parity.java.menus.tablist;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.translator.text.MessageTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;

import java.util.LinkedHashMap;
import java.util.UUID;

public class TabListData {
    private final ExtrasPlayer player;

    @Setter
    @Getter
    private String header = " ";

    @Setter
    @Getter
    private String footer = " ";

    @Getter
    private final LinkedHashMap<UUID, PlayerListEntry> players;

    public TabListData(ExtrasPlayer player) {
        this.player = player;
        players = new LinkedHashMap<>();
    }

    public static String getPlayerListName(PlayerListEntry entry) {
        if (entry.getDisplayName() == null && entry.getProfile() != null) {
            return ChatColor.GRAY + entry.getProfile().getName();
        }
        return MessageTranslator.convertMessage(entry.getDisplayName());
    }

    public static String getPlayerListHead(PlayerListEntry entry) {
        return "https://mc-heads.net/avatar/" + entry.getProfileId() + "/8";
    }


    // Ping icons are supplied by GeyserExtrasPack
    // Matches how Minecraft Java Edition chooses the icon to use
    public static String getPingIcon(PlayerListEntry entry){
        String pingIcon = "\uF834";
        int ping = entry.getLatency();
        if (ping < 0) {
            pingIcon = "\uF835";
        } else if (ping < 150) {
            pingIcon = "\uF830";
        } else if (ping < 300) {
            pingIcon = "\uF831";
        } else if (ping < 600) {
            pingIcon = "\uF832";
        } else if (ping < 1000) {
            pingIcon = "\uF833";
        }
        return pingIcon;
    }
}
