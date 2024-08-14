package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
import dev.letsgoaway.geyserextras.core.menus.SettingsAndBindingsMenu;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.CooldownUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsSection {
    public static void build(SettingsAndBindingsMenu menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new Label(ChatColor.BOLD + "Settings\n"));

        if (CooldownUtils.getDefaultShowCooldown() != CooldownUtils.CooldownType.DISABLED) {
            HashMap<String, CooldownUtils.CooldownType> cooldownTypes = new HashMap<>();
            for (CooldownUtils.CooldownType cooldownType : CooldownUtils.CooldownType.values()) {
                cooldownTypes.put(translateCooldown(cooldownType, session), cooldownType);
            }
            String playerOption = translateCooldown(session.getPreferencesCache().getCooldownPreference(), session);
            menu.add(new Dropdown(MinecraftLocale.getLocaleStringIfPresent("options.attackIndicator", session.locale()),
                    new ArrayList<>(cooldownTypes.keySet()), playerOption, (str) -> {
                session.getPreferencesCache().setCooldownPreference(cooldownTypes.get(str));
            }));
            menu.add(new Slider("Attack Indicator FPS", 5, 250, 5, player.getPreferences().getIndicatorUpdateRate(), player::startCombatTickThread));
        }

        if (session.getPreferencesCache().isAllowShowCoordinates()) {
            menu.add(new Toggle("%createWorldScreen.showCoordinates", session.getPreferencesCache().isPrefersShowCoordinates(), (b) -> {
                session.getPreferencesCache().setPrefersShowCoordinates(b);
            }));
        }

        menu.add(new Toggle("Advanced Tooltips", session.isAdvancedTooltips(), (b) -> {
            if (b != session.isAdvancedTooltips()) {
                session.setAdvancedTooltips(b);
                String onOrOff = session.isAdvancedTooltips() ? "on" : "off";
                session.sendMessage(ChatColor.BOLD + ChatColor.YELLOW
                        + MinecraftLocale.getLocaleString("debug.prefix", session.locale())
                        + " " + ChatColor.RESET
                        + MinecraftLocale.getLocaleString("debug.advanced_tooltips." + onOrOff, session.locale()));
                session.getInventoryTranslator().updateInventory(session, session.getPlayerInventory());
            }
        }));

        menu.add(new Toggle(GeyserLocale.getPlayerLocaleString("geyser.settings.option.customSkulls", session.locale()),
                session.getPreferencesCache().isPrefersCustomSkulls(),
                (b) -> session.getPreferencesCache().setPrefersCustomSkulls(b)));
    }

    private static String translateCooldown(CooldownUtils.CooldownType cooldownType, GeyserSession session) {
        switch (cooldownType) {
            case TITLE -> {
                return MinecraftLocale.getLocaleStringIfPresent("options.attack.crosshair", session.locale());
            }
            case ACTIONBAR -> {
                return MinecraftLocale.getLocaleStringIfPresent("options.attack.hotbar", session.locale());
            }
            case DISABLED -> {
                return MinecraftLocale.getLocaleStringIfPresent("options.off", session.locale());
            }
        }
        return "";
    }
}
