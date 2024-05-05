package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.APIType;
import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.Config;
import dev.letsgoaway.geyserextras.GeyserExtras;
import dev.letsgoaway.geyserextras.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.form.elements.Button;
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
            GeyserExtras.bedrockAPI.reconnect(bplayer.player.getUniqueId());
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
        if (GeyserExtras.bedrockAPI.apiInstances.containsKey(APIType.GEYSER) || Config.proxyMode) {
            add(new Button("Quick-Menu", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_base.png", () -> {
                new QuickMenu(bplayer).show(bplayer);
            }));
        }
        if (!Config.packsArray.isEmpty()) {
            if (GeyserExtras.bedrockAPI.apiInstances.containsKey(APIType.GEYSER)) {
                add(new Button("Resource Packs", FormImage.Type.PATH, "textures/ui/glyph_resource_pack.png", () -> {
                    new OptionalPacks(bplayer).show(bplayer);
                }));
            }
        }
        add(new Button("Settings", FormImage.Type.PATH, "textures/ui/settings_glyph_color_2x.png", () -> {
            new SettingsMenu(bplayer).show(bplayer);
        }));
        this.show(bplayer);
    }

}
