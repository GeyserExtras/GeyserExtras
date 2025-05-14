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
    @Getter
    private final LinkedHashMap<UUID, PlayerListEntry> players;
    @Setter
    @Getter
    private String header = " ";
    @Setter
    @Getter
    private String footer = " ";

    public TabListData(ExtrasPlayer player) {
        this.player = player;
        players = new LinkedHashMap<>();
    }

    public static String getPlayerListName(PlayerListEntry entry) {
        if (entry.getDisplayName() == null && entry.getProfile() != null) {
            return ChatColor.DARK_GRAY + entry.getProfile().getName();
        }
        return MessageTranslator.convertMessage(entry.getDisplayName());
    }

    public static String getPlayerListHead(PlayerListEntry entry) {
        return "https://starlightskins.lunareclipse.studio/render/pixel/" + getPlayerListID(entry.getProfileId()) + "/face?cameraWidth=8";
    }


    private static String getPlayerListID(UUID profileID){
        // This is how we get the XUID for Bedrock Players, even if they are joined via a different geyser proxy.
        // Firstly, im going to use my Java UUID as an example:
        // 16ea03b2-6d37-482b-9e4e-a4b42067ab84
        // This is my Java UUID for the LetsGoAway account.
        // I'm also going to use jeb_'s account as an example uuid:
        // 853c80ef-3c37-49fd-aa49-938b674adae6

        // Notice that the 3rd section of Java UUID's begin with a 4, stating that it is a version 4 UUID.
        // But if you take a look at any Floodgate Bedrock UUID (using mine for LetsGoAway3419 as an example):
        // you have this:
        // 00000000-0000-0000-0009-01f5e8f1f3d1
        // GeyserMC does not set the version number of the uuid, so the third section always starts with 0.
        // Whether or not this is a bug is up to discussion, but it allows us to do this:
        boolean isFloodgateID = profileID.version() == 0;
        if (isFloodgateID) {
            // This results in my XUID which is 2535430477181905, as Floodgate just encodes the XUID into the UUID,
            // and the face renderer server will recognize this as it supports floodgate xuids.
            // Also StarlightStudios needs a dot infront of it to recognize it as a XUID.
            return "." + profileID.getLeastSignificantBits();
        }
        // If its not a Bedrock Player, then we can just return the UUID as a string.
        return profileID.toString();
    }

    // Ping icons are supplied by GeyserExtrasPack
    // Matches how Minecraft Java Edition chooses the icon to use
    public static String getPingIcon(PlayerListEntry entry) {
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
