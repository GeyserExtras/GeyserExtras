package dev.letsgoaway.geyserextras.core.parity.java;

import dev.letsgoaway.geyserextras.MathUtils;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Setter;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class CooldownHandler {
    private ExtrasPlayer player;

    @Setter
    private long lastSwingTime;
    public CooldownHandler(ExtrasPlayer player) {
        this.player = player;
        lastSwingTime = System.currentTimeMillis();
    }

    public void tick() {
        long time = System.currentTimeMillis() - lastSwingTime;
        double cooldown = MathUtils.restrain(((double) time) * player.getAttackSpeed() / 1000d, 1);
    }
}
