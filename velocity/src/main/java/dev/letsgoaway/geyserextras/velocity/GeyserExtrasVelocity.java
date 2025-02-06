package dev.letsgoaway.geyserextras.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.Server;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.TickUtil;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.GeyserExtras;
import dev.letsgoaway.geyserextras.core.commands.CommandExecutor;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import dev.letsgoaway.geyserextras.core.preferences.JavaPreferencesData;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.entity.type.player.PlayerEntity;
import org.geysermc.geyser.level.JavaDimension;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

@Plugin(authors = "LetsGoAway", name = "GeyserExtras", version = PluginVersion.GE_VERSION, id = "geyserextras", dependencies = {@Dependency(id = "geyser"),})
public class GeyserExtrasVelocity implements Server {
    public static GeyserExtras CORE;
    public static GeyserExtrasVelocity VELOCITY;
    public static ProxyServer server;
    private final Logger logger;

    private final VelocityTickUtil velocityTickUtil;

    private final Path dataDirectory;

    @Inject
    public GeyserExtrasVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        ServerType.type = ServerType.VELOCITY;
        VELOCITY = this;
        GeyserExtrasVelocity.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        velocityTickUtil = new VelocityTickUtil();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CORE = new GeyserExtras(this);
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent ev) {
        CORE.autoReconnectAll();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent ev) {
        CORE.autoReconnectAll();
    }

    @Override
    public void onGeyserExtrasInitialize() {
        CommandManager commandManager = server.getCommandManager();
        for (String label : CommandExecutor.ids.keySet()) {
            commandManager.register(commandManager.metaBuilder(label).build(), new VelocityCommandHandler());
        }

        server.getEventManager().register(this, new VelocityListener());
    }

    @Override
    public TickUtil getTickUtil() {
        return velocityTickUtil;
    }

    @Override
    public ExtrasPlayer createPlayer(GeyserConnection connection) {
        return new VelocityExtrasPlayer(connection);
    }

    @Override
    public void log(String string) {
        this.logger.info(string);
    }

    @Override
    public void warn(String string) {
        this.logger.warn(string);
    }

    @Override
    public Path getPluginFolder() {
        return dataDirectory;
    }

    @Override
    public void sendEmoteChat(ExtrasPlayer player, String emoteChat) {
        VelocityExtrasPlayer velocityPlayer = (VelocityExtrasPlayer) player;
        Vector3f playerPos = velocityPlayer.getSession().getPlayerEntity().getPosition();
        JavaDimension dimension = velocityPlayer.getSession().getDimensionType();

        for (Player playerNear : getServerPlayers(velocityPlayer.player)) {
            UUID javaUUID = playerNear.getUniqueId();

            boolean isBedrock = GeyserExtras.GE.geyserApi.isBedrockPlayer(javaUUID);
            if (isBedrock) continue;

            JavaPreferencesData userPrefs = GE.getJavaPreferencesData(javaUUID);
            if (userPrefs != null && userPrefs.muteEmoteChat) continue;

            PlayerEntity entity = velocityPlayer.getSession().getEntityCache().getPlayerEntity(javaUUID);
            if (entity == null) continue;

            JavaDimension playerNearDimension = player.getPlayerDimensionsMap().get(entity.getEntityId());
            if (playerNearDimension != dimension) continue;

            if (EmoteUtils.EMOTE_DISTANCE >= playerPos.distance(entity.getPosition())) {
                playerNear.sendMessage(LegacyComponentSerializer.legacySection().deserialize(emoteChat));
            }
        }
    }

    private Collection<Player> getServerPlayers(Player player) {
        ServerConnection server;
        Optional<ServerConnection> connectionOptional = player.getCurrentServer();
        if (connectionOptional.isPresent()) {
            server = connectionOptional.get();
            return server.getServer().getPlayersConnected();
        }
        return Collections.emptySet();
    }

    @Override
    public void sendRawMessage(UUID javaPlayer, String message) {
        Optional<Player> optPlayer = server.getPlayer(javaPlayer);
        optPlayer.ifPresent(player -> player.sendPlainMessage(message));
    }
}
