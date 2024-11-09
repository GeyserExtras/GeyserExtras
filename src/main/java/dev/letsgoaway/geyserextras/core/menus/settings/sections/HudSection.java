package dev.letsgoaway.geyserextras.core.menus.settings.sections;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.*;
import dev.letsgoaway.geyserextras.core.preferences.Perspectives;
import dev.letsgoaway.geyserextras.core.utils.GUIElements;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class HudSection extends Section {
    @Override
    public void build(BedrockForm menu, GeyserSession session, ExtrasPlayer player) {
        for (GUIElements element : GUIElements.values()) {
            if (GE.getConfig().isDisablePaperDoll() && element.equals(GUIElements.PAPER_DOLL)) continue;
            menu.add(createToggle(element, player));
        }
    }

    @Override
    public List<String> getHeader(ExtrasPlayer player) {
        return List.of("HUD", "");
    }

    private static Toggle createToggle(GUIElements element, ExtrasPlayer player) {
        GeyserSession session = player.getSession();
        return new Toggle(element.translateOptions(player),
                element.isHidden(session), (hide) -> {
            if (hide && !element.isHidden(session)) {
                element.hide(session);
            } else if (!hide && element.isHidden(session)) {
                element.show(session);
            }
        });
    }

    @Override
    public FormImage getImage() {
        return FormImage.of(FormImage.Type.PATH, "textures/ui/heart_new.png");
    }
}
