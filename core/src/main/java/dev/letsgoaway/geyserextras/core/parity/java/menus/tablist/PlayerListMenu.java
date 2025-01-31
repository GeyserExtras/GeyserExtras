package dev.letsgoaway.geyserextras.core.parity.java.menus.tablist;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;

import java.util.stream.Stream;

// TODO: Fix header and footer
public class PlayerListMenu extends BedrockMenu {
    @Override
    public SimpleForm create(ExtrasPlayer player) {
        setTitle(player.translate("key.playerlist"));
        setHeader(player.getTabListData().getHeader() + "\n\n" + player.getTabListData().getFooter());
        for (PlayerListEntry entry : player.getTabListData().getPlayers().values()) {
            if (!entry.isListed()) continue;
            String name = fixUnreadable(createPlayerName(entry));
            add(new Button(name, FormImage.Type.URL, TabListData.getPlayerListHead(entry), () -> {
                player.sendForm(new PlayerListMenu());
            }));
        }

        return super.create(player);
    }

    private String createPlayerName(PlayerListEntry entry) {
        StringBuilder builder = new StringBuilder(TabListData.getPlayerListName(entry));
        while (builder.length() < 30) {
            builder.append(" ");
        }
        builder.append(TabListData.getPingIcon(entry));
        return builder.toString();
    }

    private static String fixUnreadable(String text) {
        return text.replace(ChatColor.GRAY, ChatColor.DARK_GRAY);
    }
}
