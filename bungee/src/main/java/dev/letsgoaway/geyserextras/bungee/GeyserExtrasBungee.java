package dev.letsgoaway.geyserextras.bungee;

import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.utils.TickUtil;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import dev.letsgoaway.geyserextras.core.commands.CommandExecutor;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import dev.letsgoaway.geyserextras.core.preferences.JavaPreferencesData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.entity.type.player.PlayerEntity;
import org.geysermc.geyser.level.JavaDimension;

import java.nio.file.Path;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class GeyserExtrasBungee extends Plugin implements Server {
    public static GeyserExtras CORE;
    public static Plugin BUNGEE;
    private static BungeeTickUtil bungeeTickUtil;

    public GeyserExtrasBungee() {
        ServerType.type = ServerType.BUNGEECORD;
        BUNGEE = this;
        bungeeTickUtil = new BungeeTickUtil();
    }

    @Override
    public void onEnable() {
        CORE = new GeyserExtras(this);
    }

    @Override
    public void onDisable() {
        CORE.autoReconnectAll();
    }

    @Override
    public void onGeyserExtrasInitialize() {
        for (String label : CommandExecutor.ids.keySet()) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeCommandHandler(label));
        }
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeListener());
    }

    @Override
    public TickUtil getTickUtil() {
        return bungeeTickUtil;
    }

    @Override
    public ExtrasPlayer createPlayer(GeyserConnection connection) {
        return new BungeeExtrasPlayer(connection);
    }

    @Override
    public void log(String string) {
        this.getLogger().info(string);
    }

    @Override
    public void warn(String string) {
        this.getLogger().warning(string);
    }

    @Override
    public Path getPluginFolder() {
        return getDataFolder().toPath();
    }

    @Override
    public void sendEmoteChat(ExtrasPlayer player, String emoteChat) {
        BungeeExtrasPlayer bungeePlayer = (BungeeExtrasPlayer) player;
        Vector3f playerPos = bungeePlayer.getSession().getPlayerEntity().getPosition();

        TextComponent component = new TextComponent();
        component.setText(ChatColor.translateAlternateColorCodes('§', emoteChat));

        JavaDimension dimension = bungeePlayer.getSession().getDimensionType();
        for (ProxiedPlayer playerNear : bungeePlayer.player.getServer().getInfo().getPlayers()) {
            UUID javaUUID = playerNear.getUniqueId();

            boolean isBedrock = GE.geyserApi.isBedrockPlayer(javaUUID);
            if (isBedrock) continue;

            JavaPreferencesData userPrefs = GE.getJavaPreferencesData(javaUUID);
            if (userPrefs != null && userPrefs.muteEmoteChat) continue;

            PlayerEntity entity = bungeePlayer.getSession().getEntityCache().getPlayerEntity(javaUUID);
            if (entity == null) continue;

            JavaDimension playerNearDimension = player.getPlayerDimensionsMap().get(entity.getEntityId());
            if (playerNearDimension != dimension) continue;

            if (EmoteUtils.EMOTE_DISTANCE >= playerPos.distance(entity.getPosition())) {
                playerNear.sendMessage(ChatMessageType.SYSTEM, component);
            }
        }
    }

    @Override
    public void sendRawMessage(UUID javaPlayer, String message) {
        ProxyServer.getInstance().getPlayer(javaPlayer).sendMessage(message);
    }
    @Override
    public void sendMessage(UUID javaPlayer, String message) {
        TextComponent component = new TextComponent();
        component.setText(ChatColor.translateAlternateColorCodes('§', message));
        ProxyServer.getInstance().getPlayer(javaPlayer).sendMessage(ChatMessageType.SYSTEM, component);
    }
}
