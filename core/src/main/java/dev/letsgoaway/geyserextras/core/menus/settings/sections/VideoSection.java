package dev.letsgoaway.geyserextras.core.menus.settings.sections;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.form.elements.MappedDropdown;
import dev.letsgoaway.geyserextras.core.form.elements.Slider;
import dev.letsgoaway.geyserextras.core.form.elements.Toggle;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.preferences.Perspectives;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.util.CooldownUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class VideoSection extends Section {
    private static String translateCooldown(CooldownUtils.CooldownType cooldownType, ExtrasPlayer player) {
        switch (cooldownType) {
            case TITLE -> {
                return BedrockLocale.CROSSHAIR;
            }
            case ACTIONBAR -> {
                return player.translate("options.attack.hotbar");
            }
            case DISABLED -> {
                return BedrockLocale.OPTIONS.OFF;
            }
        }
        return "";
    }

    @Override
    public void build(BedrockForm menu, GeyserSession session, ExtrasPlayer player) {
        if (CooldownUtils.getDefaultShowCooldown() != CooldownUtils.CooldownType.DISABLED) {
            LinkedHashMap<String, CooldownUtils.CooldownType> cooldownTypes = new LinkedHashMap<>();
            for (CooldownUtils.CooldownType cooldownType : CooldownUtils.CooldownType.values()) {
                cooldownTypes.put(translateCooldown(cooldownType, player), cooldownType);
            }
            String playerOption = translateCooldown(session.getPreferencesCache().getCooldownPreference(), player);
            menu.add(new Dropdown(player.translate("options.attackIndicator"),
                    new ArrayList<>(cooldownTypes.keySet()), playerOption, (str) -> {
                session.getPreferencesCache().setCooldownPreference(cooldownTypes.get(str));
            }));
            menu.add(new Slider(player.translateGE("ge.settings.video.attackIndicatorFPS"), 5, 250, 5, player.getPreferences().getIndicatorUpdateRate(), player::startCombatTickThread));
            menu.add(new Toggle(player.translateGE("ge.settings.video.adjustCooldownWithPing"), player.getPreferences().isAdjustCooldownWithPing(), (b) -> {
                player.getPreferences().setAdjustCooldownWithPing(b);
            }));
        }
        menu.add(new MappedDropdown<>(player.translateGE("ge.settings.video.lockedCameraPerspective"),
                Perspectives.buildTranslations(session),
                player.getPreferences().getLockedPerspective(),
                (pov) -> player.getPreferences().setLockedPerspective(pov)
        ));

        if (session.getPreferencesCache().isAllowShowCoordinates()) {
            menu.add(new Toggle(BedrockLocale.SHOW_COORDINATES, session.getPreferencesCache().isPrefersShowCoordinates(), (b) -> {
                session.getPreferencesCache().setPrefersShowCoordinates(b);
                session.getPreferencesCache().updateShowCoordinates();
            }));
        }

        if (player.getDiagnostics() != null) {
            menu.add(new Toggle(player.translateGE("ge.settings.video.showFPS"), player.getPreferences().isShowFPS(), (b) -> {
                player.getPreferences().setShowFPS(b);
                if (!b && player.getFpsBossBar() != null){
                    player.getFpsBossBar().removeBossBar();
                    player.setFpsBossBar(null);
                }
            }));
        }

        menu.add(new Toggle(player.translateGE("ge.settings.video.advancedTooltips"), session.isAdvancedTooltips(), (b) -> {
            if (b != session.isAdvancedTooltips()) {
                session.setAdvancedTooltips(b);
                String onOrOff = session.isAdvancedTooltips() ? "on" : "off";
                session.sendMessage(ChatColor.BOLD + ChatColor.YELLOW
                        + player.translate("debug.prefix")
                        + " " + ChatColor.RESET
                        + player.translate("debug.advanced_tooltips." + onOrOff));
                session.getPlayerInventory().updateInventory();
            }
        }));

        menu.add(new Toggle(GeyserLocale.getPlayerLocaleString("geyser.settings.option.customSkulls", session.locale()),
                session.getPreferencesCache().isPrefersCustomSkulls(),
                (b) -> session.getPreferencesCache().setPrefersCustomSkulls(b)));
    }

    @Override
    public List<String> getHeader(ExtrasPlayer player) {
        return List.of(BedrockLocale.OPTIONS.VIDEO, "");
    }

    @Override
    public FormImage getImage() {
        return FormImage.of(FormImage.Type.PATH, "textures/ui/video_glyph_2x.png");
    }
}
