package dev.letsgoaway.geyserextras.core.menus.settings.sections;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

public class MenuSection extends Section {
    @Override
    public void build(BedrockForm menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new Toggle("Inventory Double Click for Menu Shortcut", player.getPreferences().isEnableDoubleClickShortcut(), (b) -> {
            player.getPreferences().setEnableDoubleClickShortcut(b);
        }));

        menu.add(new Slider("Double Click Time (ms)", 150, 350, 10, player.getPreferences().getDoubleClickMS(),
                (i) -> {
                    player.getPreferences().setDoubleClickMS(Math.round(i));
                }));
    }

    @Override
    public List<String> getHeader() {
        return List.of("Menu", "");
    }

    @Override
    public FormImage getImage() {
        return FormImage.of(FormImage.Type.PATH, "textures/ui/accessibility_glyph_color.png");
    }
}
