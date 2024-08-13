package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.features.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
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

        add(new Label(ChatColor.BOLD + "Settings\n"));

        if (CooldownUtils.getDefaultShowCooldown() != CooldownUtils.CooldownType.DISABLED) {
            HashMap<String, CooldownUtils.CooldownType> cooldownTypes = new HashMap<>();
            for (CooldownUtils.CooldownType cooldownType : CooldownUtils.CooldownType.values()) {
                cooldownTypes.put(translateCooldown(cooldownType, session), cooldownType);
            }
            String playerOption = translateCooldown(session.getPreferencesCache().getCooldownPreference(), session);
            add(new Dropdown(MinecraftLocale.getLocaleStringIfPresent("options.attackIndicator", session.locale()),
                    new ArrayList<>(cooldownTypes.keySet()), playerOption, (str) -> {
                session.getPreferencesCache().setCooldownPreference(cooldownTypes.get(str));
            }));
            add(new Slider("Attack Indicator FPS", 5, 250, 5, player.getPreferences().getIndicatorUpdateRate(), player::startCombatTickThread));
        }

        if (session.getPreferencesCache().isAllowShowCoordinates()) {
            add(new Toggle("%createWorldScreen.showCoordinates", session.getPreferencesCache().isPrefersShowCoordinates(), (b) -> {
                session.getPreferencesCache().setPrefersShowCoordinates(b);
            }));
        }

        add(new Toggle("Advanced Tooltips", session.isAdvancedTooltips(), (b) -> {
            session.setAdvancedTooltips(b);
            String onOrOff = session.isAdvancedTooltips() ? "on" : "off";
            session.sendMessage(ChatColor.BOLD + ChatColor.YELLOW
                    + MinecraftLocale.getLocaleString("debug.prefix", session.locale())
                    + " " + ChatColor.RESET
                    + MinecraftLocale.getLocaleString("debug.advanced_tooltips." + onOrOff, session.locale()));
            session.getInventoryTranslator().updateInventory(session, session.getPlayerInventory());
        }));

        add(new Toggle(GeyserLocale.getPlayerLocaleString("geyser.settings.option.customSkulls", session.locale()),
                session.getPreferencesCache().isPrefersCustomSkulls(),
                (b) -> session.getPreferencesCache().setPrefersCustomSkulls(b)));


        add(new Label(ChatColor.BOLD + "Bindings\n"));

        HashMap<String, Action> actionNames = new HashMap<>();
        for (Action action : Action.values()) {
            actionNames.put(action.getLocaleString(player), action);
        }
        for (Remappable binding : Remappable.values()) {
            add(new Dropdown(binding.getLocaleString(player),
                    new ArrayList<>(actionNames.keySet()),
                    player.getPreferences().getAction(binding).getLocaleString(player), (str) -> {
                player.getPreferences().setAction(binding, actionNames.get(str));
            }));
        }
/*
        add(new Label(ChatColor.BOLD + "Debug\n" + ChatColor.RESET + ChatColor.ITALIC + ChatColor.DARK_GRAY +
                "(Dont change unless you know what you are doing!)"));

        HashMap<String, Menus> menuNames = new HashMap<>();
        for (Menus menu : Menus.values()) {
            menuNames.put(menu.name(), menu);
        }
        add(new Dropdown("Settings Menu Form:",
                new ArrayList<>(menuNames.keySet()),
                player.getPreferences().getSettingsMenuForm().name(),
                (str) -> player.getPreferences().setSettingsMenuForm(Menus.valueOf(str))
        ));

 */
        return super.create(player);
    }

    private String translateCooldown(CooldownUtils.CooldownType cooldownType, GeyserSession session) {
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
