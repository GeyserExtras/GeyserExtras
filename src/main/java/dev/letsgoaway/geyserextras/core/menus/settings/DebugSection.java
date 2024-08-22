package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.*;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import dev.letsgoaway.geyserextras.core.menus.Menus;
import dev.letsgoaway.geyserextras.core.menus.SettingsMenu;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.auth.BedrockClientData;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugSection {
    public static void build(SettingsMenu menu, GeyserSession session, ExtrasPlayer player) {

        menu.add(new SectionLabel(BedrockLocale.OPTIONS.DEBUG,
                "Don't change settings here unless you know what you are doing!"));

        HashMap<String, Menus> menuNames = new HashMap<>();
        for (Menus menuOption : Menus.values()) {
            menuNames.put(menuOption.name(), menuOption);
        }
        menu.add(new Dropdown("Settings Menu Form:",
                new ArrayList<>(menuNames.keySet()),
                player.getPreferences().getSettingsMenuForm().name(),
                (str) -> player.getPreferences().setSettingsMenuForm(Menus.valueOf(str))
        ));
        BedrockClientData data = session.getClientData();
        menu.add(new SubSectionLabel("Client Data", ""));
        menu.add(new Label("Game Version: " + data.getGameVersion()));
        menu.add(new Label("Username: " + data.getUsername()));
        menu.add(new Label("Offline ID: " + data.getPlatformOfflineId()));
        menu.add(new Label("Online ID: " + data.getPlatformOnlineId()));
        menu.add(new Label("Playfab ID: " + data.getPlayFabId()));
        menu.add(new Label("Device ID: " + data.getDeviceId()));
        menu.add(new Label("Device Model: " + data.getDeviceModel()));
        menu.add(new Label("Device OS: " + data.getDeviceOs().name()));
        menu.add(new Label("Current Input Mode: " + data.getCurrentInputMode().name()));
        menu.add(new Label("Default Input Mode: " + data.getDefaultInputMode().name()));
        menu.add(new Label("UI Profile: " + data.getUiProfile().name()));
        menu.add(new Label("GUI Scale: " + data.getGuiScale()));
        menu.add(new Label("Joined Address: " + data.getServerAddress()));
        menu.add(new Label("Language Code: " + data.getLanguageCode()));

        // Cape ID is appended to the Skin ID
        menu.add(new Label("Skin ID: " + data.getSkinId().replace(data.getCapeId(),"")));
        menu.add(new Label("Skin Color: " + data.getSkinColor()));
        menu.add(new Label("Skin Arm Size: " + data.getArmSize()));
        menu.add(new Label("Cape ID: " + data.getCapeId()));
        menu.add(new Label("Is Cape on Classic Skin? " + data.isCapeOnClassicSkin()));
        menu.add(new Label("Is Persona Skin? " + data.isPersonaSkin()));
        menu.add(new Label("Is Premium Skin? " + data.isPremiumSkin()));
    }
}
