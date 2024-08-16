package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;

public class VRInventoryMenu extends BedrockMenu {
    @Override
    public SimpleForm create(ExtrasPlayer player) {
        setTitle("VR Quick-Menu");
        add(new Button(Action.OPEN_INVENTORY.getLocaleString(player), FormImage.Type.PATH, "textures/ui/inventory_icon.png", () -> {
            Action.OPEN_INVENTORY.run(player);
        }));
        add(new Button(Action.SWAP_OFFHAND.getLocaleString(player), FormImage.Type.PATH, "textures/ui/move.png", () -> {
            Action.SWAP_OFFHAND.run(player);
        }));
        add(new Button(Action.OPEN_GE_MENU.getLocaleString(player), FormImage.Type.URL, "https://cdn.modrinth.com/data/kOfJBurB/2592d098bd31c083365e1c8d831446220995f1d7.png", () -> {
            Action.OPEN_GE_MENU.run(player);
        }));
        return super.create(player);
    }
}
