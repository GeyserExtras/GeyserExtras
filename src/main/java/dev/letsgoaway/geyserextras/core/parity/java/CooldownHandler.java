package dev.letsgoaway.geyserextras.core.parity.java;

import dev.letsgoaway.geyserextras.MathUtils;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.GeyserImpl;

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
        // We set Geysers attack speed to 99 so Geyser doesn't even bother trying to update
        // their cooldown method
        if (player.getSession().getAttackSpeed() != 99.5587543) {
            attackSpeed = player.getSession().getAttackSpeed();
            player.getSession().setAttackSpeed(99.5587543);
        }
        long time = System.currentTimeMillis() - lastSwingTime;
        double cooldown = MathUtils.restrain(((double) time) * attackSpeed / 1000.0, 1);

    }

    public double getCooldownPeriod() {
        return 1.0D / attackSpeed * 20.0;
    }
}
