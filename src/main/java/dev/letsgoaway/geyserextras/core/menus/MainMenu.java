package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.features.bindings.Action;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.ChatColor;
import org.geysermc.geyser.text.MinecraftLocale;
import org.geysermc.mcprotocollib.protocol.data.game.ClientCommand;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundClientCommandPacket;

public class MainMenu extends BedrockMenu {
    public MainMenu() {
        super();
    }

    @Override
    public SimpleForm create(ExtrasPlayer player) {
        setTitle("GeyserExtras Menu");
        GeyserSession session = player.getSession();
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


        add(new Button("Bindings", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_base.png", () -> {
            player.sendForm(new BindingsMenu());
        }));


        add(new Button("Resource Packs", FormImage.Type.PATH, "textures/ui/glyph_resource_pack.png", () -> {
            // TODO
            //new OptionalPacks(bplayer).show(bplayer);
        }));

        add(new Button("Settings", FormImage.Type.PATH, "textures/ui/settings_glyph_color_2x.png", () -> {
            //new SettingsMenu(bplayer).show(bplayer);
        }));
        return super.create(player);
    }
}
