package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;

public class MainMenu extends BedrockMenu {
    public MainMenu() {
        super();
    }

    @Override
    public SimpleForm create(ExtrasPlayer player) {
        setTitle("GeyserExtras Menu");
        add(new Button(Action.SWAP_OFFHAND.getLocaleString(player), FormImage.Type.PATH, "textures/ui/move.png", () -> {
            Action.SWAP_OFFHAND.run(player);
        }));

        add(new Button(Action.RECONNECT.getLocaleString(player), FormImage.Type.PATH, "textures/ui/refresh_hover.png", () -> {
            Action.RECONNECT.run(player);
        }));

        add(new Button(Action.TOGGLE_TOOLTIPS.getLocaleString(player), FormImage.Type.PATH, "textures/ui/infobulb.png", () -> {
            Action.TOGGLE_TOOLTIPS.run(player);
        }));

        add(new Button(Action.OPEN_ADVANCEMENTS.getLocaleString(player), FormImage.Type.PATH, "textures/ui/achievements.png", () -> {
            Action.OPEN_ADVANCEMENTS.run(player);
        }));

        add(new Button(Action.OPEN_STATISTICS.getLocaleString(player), FormImage.Type.PATH, "textures/ui/world_glyph_color_2x_black_outline.png", () -> {
            Action.OPEN_STATISTICS.run(player);
        }));

        add(new Button(Action.PLAYER_LIST.getLocaleString(player), FormImage.Type.PATH, "textures/ui/Local.png", () -> {
            Action.PLAYER_LIST.run(player);
        }));


        add(new Button("Bindings And Settings", FormImage.Type.PATH, "textures/ui/settings_glyph_color_2x.png", () -> {
            player.sendForm(new SettingsAndBindingsMenu());
        }));


        add(new Button("Resource Packs", FormImage.Type.PATH, "textures/ui/glyph_resource_pack.png", () -> {
            // TODO
            //new OptionalPacks(bplayer).show(bplayer);
        }));
        return super.create(player);
    }
}
