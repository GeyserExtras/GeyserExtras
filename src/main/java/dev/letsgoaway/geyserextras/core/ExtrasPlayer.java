package dev.letsgoaway.geyserextras.core;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import dev.letsgoaway.geyserextras.core.parity.java.CooldownHandler;
import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;

import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class ExtrasPlayer {
    private UUID javaUUID;
    private String bedrockXUID;

    @Getter
    private GeyserSession session;

    @Getter
    private CooldownHandler cooldownHandler;

    @Getter
    public User packetUser;

    public ExtrasPlayer(GeyserConnection connection) {
        this.session = (GeyserSession) connection;
        this.javaUUID = connection.javaUuid();
        this.bedrockXUID = connection.xuid();
        cooldownHandler = new CooldownHandler(this);
        for (User user : PacketEvents.getAPI().getProtocolManager().getUsers()) {
            if (user.getUUID().equals(this.javaUUID)) {
                packetUser = user;
                break;
            }
        }
        setTickingState(20.0f);
    }
    public void onDisconnect() {}
    public void onEmoteEvent(ClientEmoteEvent ev) {}

    @Setter
    @Getter
    public float tickrate = 20.0f;

    public long ticks = 0;

    public void tick() {
        ticks++;
        if (Config.customCoolDownEnabled) {
            cooldownHandler.tick();
        }
        if (Config.disablePaperDoll) {
            session.camera().hideElement(GuiElement.PAPER_DOLL);
        }
    }

    public void setTickingState(float tickrate) {
        this.tickrate = tickrate;
    }

}
