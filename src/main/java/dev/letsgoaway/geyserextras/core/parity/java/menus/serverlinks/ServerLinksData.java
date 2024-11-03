package dev.letsgoaway.geyserextras.core.parity.java.menus.serverlinks;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.translator.text.MessageTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.ServerLink;

import java.util.List;

public class ServerLinksData {
    @Setter
    @Getter
    public List<ServerLink> serverLinks = List.of();

    private final ExtrasPlayer player;

    public ServerLinksData(ExtrasPlayer player) {
        this.player = player;
    }

    public static String getLinkText(ServerLink link, ExtrasPlayer player) {
        if (link.knownType() != null) {
            switch (link.knownType()) {
                // life is unfair
                case BUG_REPORT -> {
                    return player.translate("known_server_link.report_bug");
                }
                default -> {
                    return player.translate("known_server_link." + link.knownType().name().toLowerCase());
                }
            }
        } else {
            if (IsAvailable.adventure()) {
                return MessageTranslator.convertMessage(link.unknownType());
            }
            // TODO fix this with reflection
            else {
                return "adventure_not_found";
            }
        }
    }
}
