package dev.letsgoaway.geyserextras.core;

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
    @Getter
    private UUID javaUUID;

    @Getter
    private String bedrockXUID;

    @Getter
    public GeyserSession session;

    @Getter
    private CooldownHandler cooldownHandler;

    public ExtrasPlayer(GeyserConnection connection) {
        this.session = (GeyserSession) connection;
        this.javaUUID = connection.javaUuid();
        this.bedrockXUID = connection.xuid();
        cooldownHandler = new CooldownHandler(this);
    }

    public void onDisconnect() {
    }

    public void onEmoteEvent(ClientEmoteEvent ev) {
    }

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

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {

        SetTitlePacket timesPacket = new SetTitlePacket();
        timesPacket.setText("");
        timesPacket.setType(SetTitlePacket.Type.TIMES);
        timesPacket.setFadeInTime(fadeIn);
        timesPacket.setStayTime(stay);
        timesPacket.setFadeOutTime(fadeOut);
        timesPacket.setXuid("");
        timesPacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(timesPacket);
        SetTitlePacket titlePacket = new SetTitlePacket();
        titlePacket.setType(SetTitlePacket.Type.TITLE);
        titlePacket.setText(title.isEmpty() ? " " : "");
        titlePacket.setXuid("");
        titlePacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(titlePacket);
        SetTitlePacket subtitlePacket = new SetTitlePacket();
        subtitlePacket.setType(SetTitlePacket.Type.SUBTITLE);
        subtitlePacket.setText(subtitle);
        subtitlePacket.setXuid("");
        subtitlePacket.setPlatformOnlineId("");
        session.sendUpstreamPacket(subtitlePacket);

    }

    public void setTickingState(float tickrate) {
        this.tickrate = tickrate;
    }

}
