package dev.letsgoaway.geyserextras.core.utils;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
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

    public String translateOptions(ExtrasPlayer player) {
        String hide = player.translateGE("ge.settings.hud.hide");
        switch (this) {
            default -> {
                return hide.replace("%s", player.translateGE("ge.settings.hud." + this.name().toLowerCase()));
            }
            // lololol ez translation
            case PAPER_DOLL -> {
                return BedrockLocale.OPTIONS.HIDE_PAPER_DOLL;
            }
        }
    }
}
