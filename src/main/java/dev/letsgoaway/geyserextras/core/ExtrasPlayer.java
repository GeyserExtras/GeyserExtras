package dev.letsgoaway.geyserextras.core;

import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;

import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class ExtrasPlayer {
    private UUID javaUUID;
    private String bedrockXUID;

    @Getter
    private GeyserConnection geyserConnection;

    public ExtrasPlayer(GeyserConnection connection) {
        this.geyserConnection = connection;
        this.javaUUID = connection.javaUuid();
        this.bedrockXUID = getGeyserConnection().xuid();
    }

    public void onEmoteEvent(ClientEmoteEvent ev) {
    }

    @Setter
    public double attackSpeed = 4.0;

    @Setter
    public float tickrate = 20.0f;

    public long ticks = 0;

    public void tick() {
        ticks++;
    }

    public void setTickingState(float tickrate){
        this.tickrate = tickrate;
    }
}
