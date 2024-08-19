package dev.letsgoaway.geyserextras.core.utils;

import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.session.GeyserSession;

// Geyser GUI elements as an Enum because its easier for me to use
public enum GUIElements {
    AIR_BUBBLES_BAR(GuiElement.AIR_BUBBLES_BAR),
    ARMOR(GuiElement.ARMOR),
    CROSSHAIR(GuiElement.CROSSHAIR),
    EFFECTS_BAR(GuiElement.EFFECTS_BAR),
    PROGRESS_BAR(GuiElement.PROGRESS_BAR),
    FOOD_BAR(GuiElement.FOOD_BAR),
    HEALTH(GuiElement.HEALTH),
    HOTBAR(GuiElement.HOTBAR),
    ITEM_TEXT_POPUP(GuiElement.ITEM_TEXT_POPUP),
    VEHICLE_HEALTH(GuiElement.VEHICLE_HEALTH),
    PAPER_DOLL(GuiElement.PAPER_DOLL),
    TOOL_TIPS(GuiElement.TOOL_TIPS),
    TOUCH_CONTROLS(GuiElement.TOUCH_CONTROLS);

    private final GuiElement geyserElement;

    GUIElements(GuiElement element) {
        this.geyserElement = element;
    }

    public void hide(GeyserSession session) {
        session.camera().hideElement(this.geyserElement);
    }

    public void show(GeyserSession session) {
        session.camera().resetElement(this.geyserElement);
    }

    public boolean isHidden(GeyserSession session) {
        return session.camera().isHudElementHidden(this.geyserElement);
    }

    public String translateOptions(GeyserSession session) {
        switch (this) {
            case AIR_BUBBLES_BAR -> {
                return "Hide Air Bubbles Bar";
            }
            case ARMOR -> {
                return "Hide Armor Bar";
            }
            case CROSSHAIR -> {
                return "Hide Crosshair";
            }
            case EFFECTS_BAR -> {
                return "Hide Effects Bar";
            }
            case PROGRESS_BAR -> {
                return "Hide Experience Bar";
            }
            case FOOD_BAR -> {
                return "Hide Hunger Bar";
            }
            case HEALTH -> {
                return "Hide Health Bar";
            }
            case HOTBAR -> {
                return "Hide Hotbar";
            }
            case ITEM_TEXT_POPUP -> {
                return "Hide Item Text Popup";
            }
            case VEHICLE_HEALTH -> {
                return "Hide Mounted Entity Health";
            }
            // lololol ez translation
            case PAPER_DOLL -> {
                return BedrockLocale.OPTIONS.HIDE_PAPER_DOLL;
            }
            case TOOL_TIPS -> {
                return "Hide Tooltips";
            }
            case TOUCH_CONTROLS -> {
                return "Hide Touch Controls";
            }
        }
        return this.name();
    }
}
