package dev.letsgoaway.geyserextras.core;

import dev.letsgoaway.geyserextras.core.parity.java.CooldownHandler;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;

import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class ExtrasPlayer {
    private UUID javaUUID;
    private String bedrockXUID;

    @Getter
    private GeyserConnection geyserConnection;

    @Getter
    private CooldownHandler cooldownHandler;

    public ExtrasPlayer(GeyserConnection connection) {
        this.geyserConnection = connection;
        this.javaUUID = connection.javaUuid();
        this.bedrockXUID = getGeyserConnection().xuid();
        cooldownHandler = new CooldownHandler(this);
    }

    public void onEmoteEvent(ClientEmoteEvent ev) {

    }

    @Setter
    @Getter
    public double attackSpeed = 4.0;

    @Setter
    public float tickrate = 20.0f;

    public long ticks = 0;

    public void tick() {
        ticks++;
        cooldownHandler.tick();
        if (Config.disablePaperDoll) {
            geyserConnection.camera().hideElement(GuiElement.PAPER_DOLL);
        }
    }

    public void setTickingState(float tickrate) {
        this.tickrate = tickrate;
    }
}
