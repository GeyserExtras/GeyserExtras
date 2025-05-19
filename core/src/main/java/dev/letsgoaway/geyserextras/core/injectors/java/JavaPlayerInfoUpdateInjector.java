package dev.letsgoaway.geyserextras.core.injectors.java;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.Translator;
import org.geysermc.geyser.translator.protocol.java.entity.player.JavaPlayerInfoUpdateTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntryAction;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoUpdatePacket;

import java.util.Set;

// this could probably be better but im lazy
// TODO figure out why this doesnt remove players properly
@Translator(packet = ClientboundPlayerInfoUpdatePacket.class)
public class JavaPlayerInfoUpdateInjector extends JavaPlayerInfoUpdateTranslator {
    @Override
    public void translate(GeyserSession session, ClientboundPlayerInfoUpdatePacket packet) {
        super.translate(session, packet);
        ExtrasPlayer player = GeyserHandler.getPlayer(session);
        Set<PlayerListEntryAction> actions = packet.getActions();
        /* Updaters */
        for (PlayerListEntry entry : packet.getEntries()) {
            PlayerListEntry cached = getCached(player, entry);

            if (actions.contains(PlayerListEntryAction.UPDATE_LISTED)) {
                if (cached == null) {
                    // Add it because we havent cached it yet.
                    if (entry.isListed()) {
                        player.getTabListData().getPlayers().put(entry.getProfileId(), entry);
                    }
                } else {
                    // Edit the current listed status because the player is cached.
                    cached.setListed(entry.isListed());
                    if (entry.isListed()) {
                        player.getTabListData().getPlayers().put(entry.getProfileId(), cached);
                    } else {
                        player.getTabListData().getPlayers().remove(entry.getProfileId());
                    }
                    continue;
                }
            }

            if (cached != null) {
                if (actions.contains(PlayerListEntryAction.UPDATE_DISPLAY_NAME)) {
                    cached.setDisplayName(entry.getDisplayName());
                }
                if (actions.contains(PlayerListEntryAction.UPDATE_LATENCY)) {
                    cached.setLatency(entry.getLatency());
                }
                if (actions.contains(PlayerListEntryAction.UPDATE_GAME_MODE)) {
                    cached.setGameMode(entry.getGameMode());
                }
                player.getTabListData().getPlayers().put(entry.getProfileId(), cached);
            }
        }
    }

    private PlayerListEntry getCached(ExtrasPlayer player, PlayerListEntry entry) {
        return player.getTabListData().getPlayers().get(entry.getProfileId());
    }
}
