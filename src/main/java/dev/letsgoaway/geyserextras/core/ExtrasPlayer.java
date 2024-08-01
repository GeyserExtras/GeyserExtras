package dev.letsgoaway.geyserextras.core;

import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;

import java.util.UUID;

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
    private double attackSpeed = 4.0;

    @Setter
    private float tickrate = 20.0f;

    private long ticks = 0;

    public void tick() {
        ticks++;
    }
}
