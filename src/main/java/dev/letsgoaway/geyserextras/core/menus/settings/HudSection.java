package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.*;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.menus.SettingsMenu;
import dev.letsgoaway.geyserextras.core.preferences.Perspectives;
import dev.letsgoaway.geyserextras.core.utils.GUIElements;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class HudSection {
    public static void build(SettingsMenu menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new SectionLabel("HUD", ""));

        // TODO: Figure out why this sends a broken packet
        /*
        menu.add(new MappedDropdown<>("Lock Camera Perspective",
                Perspectives.buildTranslations(session),
                player.getPreferences().getLockedPerspective(),
                (pov) -> player.getPreferences().setLockedPerspective(pov)
        ));
        */
        for (GUIElements element : GUIElements.values()) {
            if (GE.getConfig().isDisablePaperDoll() && element.equals(GUIElements.PAPER_DOLL)) continue;
            menu.add(createToggle(element, session));
        }
    }

    private static Toggle createToggle(GUIElements element, GeyserSession session) {
        return new Toggle(element.translateOptions(session),
                element.isHidden(session), (hide) -> {
            if (hide && !element.isHidden(session)) {
                element.hide(session);
            } else if (!hide && element.isHidden(session)) {
                element.show(session);
            }
        });
    }
}
