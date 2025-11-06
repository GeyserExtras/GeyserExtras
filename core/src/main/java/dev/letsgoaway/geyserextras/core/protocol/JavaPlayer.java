package dev.letsgoaway.geyserextras.core.protocol;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import dev.letsgoaway.geyserextras.core.protocol.handlers.JavaPacketHandler;
import dev.letsgoaway.geyserextras.core.protocol.handlers.workarounds.MannequinCapeWorkaround;
import lombok.Getter;

import java.util.ArrayList;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class JavaPlayer {
    ArrayList<JavaPacketHandler> handlers = new ArrayList<>();

    @Getter
    User user;

    public JavaPlayer(User user) {
        this.user = user;
        createHandlers();
    }

    private void createHandlers() {
        if (GE.getConfig().isEnableBedrockCapesOnJavaWorkaround()) {
            handlers.add(new MannequinCapeWorkaround());
        }
    }

    public void onPacketSend(PacketSendEvent event) {
        for (JavaPacketHandler handler : this.handlers) {
            if (event.isCancelled() && !handler.runIfCancelled()) {
                continue;
            }
            handler.onPacketSend(this, event);

        }
    }


    public void onPacketReceive(PacketReceiveEvent event) {
        for (JavaPacketHandler handler : this.handlers) {
            if (event.isCancelled() && !handler.runIfCancelled()) {
                continue;
            }
            handler.onPacketReceive(this, event);
        }
    }

    public static JavaPlayer get(UUID uuid) {
        return ProtocolHandler.getJavaPlayer(uuid);
    }

    public static boolean exists(UUID uuid) {
        return ProtocolHandler.javaPlayerExists(uuid);
    }

}
