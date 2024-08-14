package dev.letsgoaway.geyserextras.core.menus.settings;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.elements.*;
import dev.letsgoaway.geyserextras.core.menus.Menus;
import dev.letsgoaway.geyserextras.core.menus.SettingsAndBindingsMenu;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;

public class HudSection {
    public static void build(SettingsAndBindingsMenu menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new Label(ChatColor.BOLD + "HUD\n"));
        menu.add(createToggle(GuiElement.AIR_BUBBLES_BAR, "Hide Air Bubbles Bar", session));
        menu.add(createToggle(GuiElement.ARMOR, "Hide Armor Bar", session));
        menu.add(createToggle(GuiElement.CROSSHAIR, "Hide Crosshair", session));
        menu.add(createToggle(GuiElement.EFFECTS_BAR, "Hide Effects Bar", session));
        menu.add(createToggle(GuiElement.PROGRESS_BAR, "Hide Experience Bar", session));
        menu.add(createToggle(GuiElement.FOOD_BAR, "Hide Hunger Bar", session));
        menu.add(createToggle(GuiElement.HEALTH, "Hide Health Bar", session));
        menu.add(createToggle(GuiElement.HOTBAR, "Hide Hotbar", session));
        menu.add(createToggle(GuiElement.ITEM_TEXT_POPUP, "Hide Item Text Popup", session));
        menu.add(createToggle(GuiElement.VEHICLE_HEALTH, "Hide Mounted Entity Health", session));
        menu.add(createToggle(GuiElement.TOOL_TIPS, "Hide Tooltips", session));
        menu.add(createToggle(GuiElement.TOUCH_CONTROLS, "Hide Touch Controls", session));
    }

    private static Toggle createToggle(GuiElement element, String name, GeyserSession session) {
        return new Toggle(name, session.camera().isHudElementHidden(element), (hide) -> {
            if (hide && !session.camera().isHudElementHidden(element)) {
                session.camera().hideElement(element);
            } else if (!hide && session.camera().isHudElementHidden(element)) {
                session.camera().resetElement(element);
            }
        });
    }
}
