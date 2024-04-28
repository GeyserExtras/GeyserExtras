package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.APIType;
import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.Config;
import dev.letsgoaway.geyserextras.GeyserExtras;
import dev.letsgoaway.geyserextras.api.BedrockPluginAPI;
import dev.letsgoaway.geyserextras.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.form.elements.Button;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OptionalPacks extends BedrockContextMenu {
    public static Map<String, String[]> loadingResourcePacks = new HashMap<>();

    public OptionalPacks(BedrockPlayer bplayer) {
        super("Optional Resource Packs");
        BedrockPluginAPI geyserApi = GeyserExtras.bedrockAPI.apiInstances.get(APIType.GEYSER);
        String xuid = GeyserExtras.bedrockAPI.getPlayerXUID(bplayer);
        this.onClose = () -> {
            if (!OptionalPacks.loadingResourcePacks.containsKey(xuid)) {
                loadingResourcePacks.putIfAbsent(xuid, bplayer.optionalPacks.toArray(String[]::new));
            }
            if (!Arrays.equals(OptionalPacks.loadingResourcePacks.get(xuid), bplayer.optionalPacks.toArray(String[]::new))) {
                loadingResourcePacks.replace(xuid, bplayer.optionalPacks.toArray(String[]::new));
                bplayer.save();
                GeyserExtras.bedrockAPI.reconnect(bplayer.player.getUniqueId());
            }

        };

        add(new Button("§b§l§nSelected", () -> {
            new OptionalPacks(bplayer).show(bplayer);
        }));
        for (String id : bplayer.optionalPacks) {
            String packName = geyserApi.getPackName(id);
            add(new Button(packName, () -> {
                new OptionalPackManager(bplayer, id, packName).show(bplayer);
            }));
        }
        add(new Button("§b§l§nAvailable", () -> {
            new OptionalPacks(bplayer).show(bplayer);
        }));
        for (File rp : Config.packsArray) {
            String id = geyserApi.getPackID(rp.toPath()).toString();
            if (!bplayer.optionalPacks.contains(id)) {
                String packName = geyserApi.getPackName(geyserApi.getPackID(rp.toPath()).toString());
                add(new Button(packName, () -> {
                    new OptionalPackManager(bplayer, geyserApi.getPackID(rp.toPath()).toString(), packName).show(bplayer);
                }));
            }
        }
    }
}
