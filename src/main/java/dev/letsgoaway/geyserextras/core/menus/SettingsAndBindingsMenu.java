package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
import dev.letsgoaway.geyserextras.core.menus.settings.BindingsSection;
import dev.letsgoaway.geyserextras.core.menus.settings.DebugSection;
import dev.letsgoaway.geyserextras.core.menus.settings.HudSection;
import dev.letsgoaway.geyserextras.core.menus.settings.SettingsSection;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.CooldownUtils;

import java.util.ArrayList;
import java.util.HashMap;

// Might rewrite dropdown form element because this is disgusting lmaooo
public class SettingsAndBindingsMenu extends BedrockForm {
    @Override
    public CustomForm.Builder create(ExtrasPlayer player) {
        setTitle("GeyserExtras Settings");
        GeyserSession session = player.getSession();
        SettingsSection.build(this, session, player);
        HudSection.build(this, session, player);
        BindingsSection.build(this, session, player);
        DebugSection.build(this, session, player);
        return super.create(player);
    }

}
