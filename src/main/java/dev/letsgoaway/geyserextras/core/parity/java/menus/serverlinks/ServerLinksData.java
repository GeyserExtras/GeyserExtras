package dev.letsgoaway.geyserextras.core.parity.java.menus.serverlinks;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.translator.text.MessageTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.ServerLink;

import java.util.ArrayList;
import java.util.List;

public class ServerLinksData {
    @Setter
    @Getter
    public List<ServerLink> serverLinks = List.of();

    private final ExtrasPlayer player;
    public ServerLinksData(ExtrasPlayer player) {
        this.player = player;
    }

    public static String getLinkText(ServerLink link, GeyserSession session) {
        if (link.knownType() != null) {
            switch (link.knownType()) {
                // life is unfair
                case BUG_REPORT -> {
                    return MinecraftLocale.getLocaleString("known_server_link.report_bug", session.locale());
                }
                default -> {
                    return MinecraftLocale.getLocaleString("known_server_link." + link.knownType().name().toLowerCase(), session.locale());
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
