package dev.letsgoaway.geyserextras.core.parity.java.menus.serverlinks;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.translator.text.MessageTranslator;
import org.geysermc.mcprotocollib.protocol.data.game.ServerLink;

import java.util.List;

public class ServerLinksMenu extends BedrockMenu {

    @Override
    public SimpleForm create(ExtrasPlayer player) {
        GeyserSession session = player.getSession();
        setTitle(MinecraftLocale.getLocaleString("menu.server_links.title", session.locale()));
        List<ServerLink> links = player.getServerLinksData().getServerLinks();
        for (ServerLink link : links) {
            String linkText = ServerLinksData.getLinkText(link, session);

            add(new Button(linkText, () -> {
                if (player.getPreferences().isPromptOnLinks()) {
                    player.sendForm(new LinkWarning(link));
                }
                else {
                    player.sendForm(new LinkMenu(link));
                }
            }));
        }
        return super.create(player);
    }
}
