package dev.letsgoaway.geyserextras.spigot.menus;

import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.GeyserExtrasSpigot;
import dev.letsgoaway.geyserextras.core.geyser.APIType;
import dev.letsgoaway.geyserextras.core.geyser.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.core.geyser.form.elements.Button;
import dev.letsgoaway.geyserextras.spigot.menus.quickmenu.QuickMenu;
import dev.letsgoaway.geyserextras.spigot.parity.java.tablist.TabList;
import org.geysermc.cumulus.util.FormImage;

public class MainMenu extends BedrockContextMenu {
    public MainMenu(BedrockPlayer bplayer) {
        super("Geyser Extras");
        if (bplayer.player.hasPermission("geyser.command.offhand")) {
            add(new Button("Offhand", FormImage.Type.PATH, "textures/ui/move.png", () -> {
                bplayer.player.performCommand("geyser offhand");
            }));
        }
        add(new Button("Reconnect", FormImage.Type.PATH, "textures/ui/refresh_hover.png", () -> {
            GeyserExtrasSpigot.bedrockAPI.reconnect(bplayer.player.getUniqueId());
        }));
        if (bplayer.player.hasPermission("geyser.command.tooltips")) {
            add(new Button("Advanced Tooltips", FormImage.Type.PATH, "textures/ui/infobulb.png", () -> {
                bplayer.player.performCommand("geyser tooltips");
            }));
        }
        if (bplayer.player.hasPermission("geyser.command.advancements")) {
            add(new Button("Advancements", FormImage.Type.PATH, "textures/ui/achievements.png", () -> {
                bplayer.player.performCommand("geyser advancements");
            }));
        }
        if (bplayer.player.hasPermission("geyser.command.statistics")) {
            add(new Button("Statistics", FormImage.Type.PATH, "textures/ui/world_glyph_color_2x_black_outline.png", () -> {
                bplayer.player.performCommand("geyser statistics");
            }));
        }
        if (bplayer.player.hasPermission("geyserextras.playerlist")) {
            add(new Button("Player List", FormImage.Type.PATH, "textures/ui/Local.png", () -> {
                new TabList(bplayer);
            }));
        }
        if (GeyserExtrasSpigot.bedrockAPI.supports(APIType.GEYSER) || Config.proxyMode) {
            add(new Button("Quick-Menu", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_base.png", () -> {
                new QuickMenu(bplayer).show(bplayer);
            }));
        }
        if (GeyserExtrasSpigot.bedrockAPI.supports(APIType.GEYSER) && !Config.packsArray.isEmpty()) {
            add(new Button("Resource Packs", FormImage.Type.PATH, "textures/ui/glyph_resource_pack.png", () -> {
                new OptionalPacks(bplayer).show(bplayer);
            }));
        }
        add(new Button("Settings", FormImage.Type.PATH, "textures/ui/settings_glyph_color_2x.png", () -> {
            new SettingsMenu(bplayer).show(bplayer);
        }));
        this.show(bplayer);
    }

}
