package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.EmoteUtils;
import dev.letsgoaway.geyserextras.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.form.elements.Button;
import org.geysermc.cumulus.util.FormImage;

import java.util.Map;

public class QuickMenu extends BedrockContextMenu {
    public QuickMenu(BedrockPlayer bplayer) {
        super("Quick-Menu");
        add(new Button("Bindings", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_base.png",()->{
            new QuickMenuSetup(bplayer).show(bplayer);
        }));
        add(new Button("Actions", FormImage.Type.PATH, "textures/ui/settings_glyph_color_2x.png",()->{
            new QuickMenuSettings(bplayer).show(bplayer);
        }));
    }
}
