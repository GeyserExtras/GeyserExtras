package dev.letsgoaway.geyserextras.spigot.parity.java.tablist;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.letsgoaway.geyserextras.spigot.BedrockPlayer;
import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.GeyserExtras;
import dev.letsgoaway.geyserextras.spigot.Tick;
import dev.letsgoaway.geyserextras.spigot.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.spigot.form.elements.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.util.FormImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// https://api.tydiumcraft.net/v1/players/skin?uuid=16ea03b2-6d37-482b-9e4e-a4b42067ab84&type=avatar
public class TabList extends BedrockContextMenu {
    public static final Map<String, String> bedrockPlayerTextureIDs = new ConcurrentHashMap<>();

    public TabList(BedrockPlayer bplayer) {
        super(getPlayerHeader(bplayer), getPlayerFooter(bplayer));
        Tick.runAsync(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                add(new Button(getPlayerListName(player), FormImage.Type.URL,
                        getFaceURL(player)
                        , () -> {
                    if (Bukkit.getOfflinePlayer(player.getUniqueId()).isOnline()) {
                        new TabListPlayerDetails(bplayer, player).show(bplayer);
                    }
                    //new TabList(bplayer);
                }));
            }
            this.show(bplayer);
        });
    }

    public static String loadTextureID(Player player) {
        if (GeyserExtras.bedrockAPI.isBedrockPlayer(player.getUniqueId())
                && !GeyserExtras.bedrockAPI.isLinked(GeyserExtras.bplayers.get(player.getUniqueId()))) {
            String XUID = GeyserExtras.bedrockAPI.getPlayerXUID(GeyserExtras.bplayers.get(player.getUniqueId()));
            if (bedrockPlayerTextureIDs.containsKey(XUID)) {
                return bedrockPlayerTextureIDs.get(XUID);
            }
            try {
                if (player.getPlayerProfile().getTextures().getSkin() != null) {
                    return player.getPlayerProfile().getTextures().getSkin().toString().replace("http://textures.minecraft.net/texture/", "");
                }
                URL url = new URL("https://api.geysermc.org/v2/skin/" + XUID);
                URLConnection request = url.openConnection();
                request.setConnectTimeout(5000);
                request.connect();
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonObject rootobj = root.getAsJsonObject();
                if (rootobj.isEmpty()) {
                    return "";
                }
                bedrockPlayerTextureIDs.put(XUID, rootobj.get("texture_id").getAsString());
                return bedrockPlayerTextureIDs.get(XUID);
            } catch (IOException e) {
                return "";
            }
        } else if (player.getPlayerProfile().getTextures().getSkin() != null) {
            return player.getPlayerProfile().getTextures().getSkin().toString().replace("http://textures.minecraft.net/texture/", "");
        } else {
            return player.getUniqueId().toString();
        }
    }

    public static String getFaceURL(Player player) {
        return "https://mc-heads.net/avatar/" + loadTextureID(player) + "/8";
    }

    public static String getBodyURL(Player player) {
        return "https://mc-heads.net/player/" + loadTextureID(player) + "/64";
    }

    public static String getSkinURL(Player player) {
        URL skin = player.getPlayerProfile().getTextures().getSkin();
        if (skin == null) {
            return "http://textures.minecraft.net/texture/" + loadTextureID(player);
        }
        return skin.toString();
    }

    private static String getPlayerHeader(BedrockPlayer bedrockPlayer) {
        String header = bedrockPlayer.player.getPlayerListHeader();
        if (header == null) {
            header = "";
        }
        return header;
    }

    private static String getPlayerFooter(BedrockPlayer bedrockPlayer) {
        String footer = bedrockPlayer.player.getPlayerListFooter();
        if (footer == null) {
            footer = "";
        }
        return footer;
    }

    private static String getPlayerListName(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(player.getDisplayName());
        while (builder.length() < 25) {
            builder.append(" ");
        }
        builder.append(" ");
        String pingIcon;
        if (player.getPing() < 0) {
            pingIcon = "\uF835";
        } else if (player.getPing() < 150) {
            pingIcon = "\uF830";
        } else if (player.getPing() < 300) {
            pingIcon = "\uF831";
        } else if (player.getPing() < 600) {
            pingIcon = "\uF832";
        } else if (player.getPing() < 1000) {
            pingIcon = "\uF833";
        } else {
            pingIcon = "\uF834";
        }
        builder.append(pingIcon);
        return builder.toString();
    }


    public static void precacheSkin(Player player) {
        Tick.runAsync(() -> {
            loadTextureID(player);
            if (Config.skinSavingEnabled) {
                URL url;
                try {
                    url = new URL(getSkinURL(player));
                    BufferedImage img = ImageIO.read(url);
                    // cooked
                    Path folder = Files.createDirectories(Path.of(Config.skinsFolder.toAbsolutePath() + "/" + player.getUniqueId()));
                    File file = new File(folder.toAbsolutePath() + "/" + loadTextureID(player) + ".png");
                    if (!file.exists()) {
                        ImageIO.write(img, "png", file);
                    }
                } catch (Exception ignored) {
                }
            }
        });

    }
}
