package dev.letsgoaway.geyserextras.core.parity.java;

import dev.letsgoaway.geyserextras.MathUtils;
import dev.letsgoaway.geyserextras.ServerType;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.registry.Registries;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class CooldownHandler {
    private ExtrasPlayer player;

    @Setter
    private long lastSwingTime;

    @Setter
    @Getter
    public double attackSpeed = 4.0;

    public CooldownHandler(ExtrasPlayer player) {
        this.player = player;
        lastSwingTime = System.currentTimeMillis();
    }


    public void tick() {
        if (player.getSession().getLastHitTime() > lastSwingTime) {
            lastSwingTime = player.getSession().getLastHitTime();
        }
        attackSpeed = player.getSession().getAttackSpeed();

        long time = System.currentTimeMillis() - lastSwingTime;
        double cooldown = MathUtils.restrain(((double) time) * attackSpeed / 1000.0, 1);
        player.sendTitle("", String.valueOf(cooldown), 0, 5, 0);
    }

    public double getCooldownPeriod() {
        return 1.0D / attackSpeed * 20.0;
    }
}
