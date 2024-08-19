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

public class HudSection {
    public static void build(SettingsMenu menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new SectionLabel("HUD\n", ""));

        // TODO: Figure out why this sends a broken packet
        /*
        menu.add(new MappedDropdown<>("Lock Camera Perspective",
                Perspectives.buildTranslations(session),
                player.getPreferences().getLockedPerspective(),
                (pov) -> player.getPreferences().setLockedPerspective(pov)
        ));
        */
        for (GUIElements element : GUIElements.values()) {
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
