package dev.letsgoaway.geyserextras.core.menus.settings.sections;

import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.geyser.util.CooldownUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SettingsSection extends Section {
    @Override
    public void build(BedrockForm menu, GeyserSession session, ExtrasPlayer player) {
        if (CooldownUtils.getDefaultShowCooldown() != CooldownUtils.CooldownType.DISABLED) {
            LinkedHashMap<String, CooldownUtils.CooldownType> cooldownTypes = new LinkedHashMap<>();
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
            menu.add(new Toggle(BedrockLocale.SHOW_COORDINATES, session.getPreferencesCache().isPrefersShowCoordinates(), (b) -> {
                session.getPreferencesCache().setPrefersShowCoordinates(b);
                session.getPreferencesCache().updateShowCoordinates();
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
                return BedrockLocale.CROSSHAIR;
            }
            case ACTIONBAR -> {
                return MinecraftLocale.getLocaleStringIfPresent("options.attack.hotbar", session.locale());
            }
            case DISABLED -> {
                return BedrockLocale.OPTIONS.OFF;
            }
        }
        return "";
    }

    public List<String> getHeader() {
        return List.of(BedrockLocale.SETTINGS, "");
    }
}
