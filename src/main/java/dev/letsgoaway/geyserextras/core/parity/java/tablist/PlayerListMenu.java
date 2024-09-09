package dev.letsgoaway.geyserextras.core.parity.java.tablist;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;

import java.util.stream.Stream;
// TODO: Fix header and footer
public class PlayerListMenu extends BedrockMenu {
    @Override
    public SimpleForm create(ExtrasPlayer player) {
        // So it can be overrided by json ui
        setTitle("geyserextras:playerlist");

        Stream<String> headerLines = player.getTabListData().getHeader().lines();
        headerLines.forEachOrdered((line) -> {
            add(new Button(line, () -> {
                player.sendForm(new PlayerListMenu());
            }));
        });

        for (PlayerListEntry entry : player.getTabListData().getPlayers().values()) {
            if (!entry.isListed()) continue;
            String name = createPlayerName(entry);
            add(new Button(name, FormImage.Type.URL, TabListData.getPlayerListHead(entry), () -> {
                player.sendForm(new PlayerListMenu());
            }));
        }

        Stream<String> footerLines = player.getTabListData().getFooter().lines();
        footerLines.forEachOrdered((line) -> {
            add(new Button(line, () -> {
                player.sendForm(new PlayerListMenu());
            }));
        });

        return super.create(player);
    }

    private String createPlayerName(PlayerListEntry entry) {
        StringBuilder builder = new StringBuilder(TabListData.getPlayerListName(entry));
        while (builder.length() < 25) {
            builder.append(" ");
        }
        builder.append(TabListData.getPingIcon(entry));
        return builder.toString();
    }
}
