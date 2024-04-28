package dev.letsgoaway.geyserextras;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.UUID;

public class CombatData {
    public Vector3f delta;

    public Location lastLocation;

    public UUID uuid;

    public CombatData(Player player) {
        lastLocation = player.getLocation().clone();
        uuid = player.getUniqueId();
        delta = new Vector3f();
    }

    public void update(Player player) {
        uuid = player.getUniqueId();
        delta.set(
                player.getLocation().getX() - lastLocation.getX(),
                player.getLocation().getY() - lastLocation.getY(),
                player.getLocation().getZ() - lastLocation.getZ()
        );
        lastLocation = player.getLocation().clone();
    }
    public static float clamp(float num, float min, float max) {
        if (num < min) {
            return min;
        } else {
            return Math.min(num, max);
        }
    }

    public static float getCooldownPeriod(Player player) {
        return (float) (1.0D / Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)).getValue() * 20.0D);
    }

    public static float getCooledAttackStrength(float adjustTicks, Player player) {
        return clamp(((getCooldownPeriod(player) * player.getAttackCooldown()) + adjustTicks) / getCooldownPeriod(player), 0.0F, 1.0F);
    }

    private void runOnNextTick(Runnable task) {
        Bukkit.getScheduler().runTaskLater(GeyserExtras.plugin,
                task
                , 1L);
    }

    public CombatAttackType onAttack(Player player, EntityDamageByEntityEvent ev) {
        if (ev.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if ((getCooledAttackStrength(0.5F, player) > 0.9f)) {
                if (player.getInventory().getItemInMainHand().getType().getTranslationKey().contains("_sword")
                        && !player.isSprinting()
                        && player.isOnGround()
                ) {
                    return CombatAttackType.SWEEP_ATTACK;
                } else if (player.getFallDistance() > 0.0f && !player.isSprinting()) {
                    return CombatAttackType.CRITICAL_ATTACK;
                } else if (player.isSprinting()) {
                    if (Config.knockbackAttackSprintFix && GeyserExtras.bedrockAPI.isBedrockPlayer(uuid)) {
                        player.setSprinting(false);
                        player.sendHealthUpdate(player.getHealth(), 0, player.getSaturation());
                        runOnNextTick(() -> {
                            player.setSprinting(false);
                            runOnNextTick(() -> {
                                player.sendHealthUpdate(player.getHealth(), player.getFoodLevel(), player.getSaturation());
                            });
                        });
                    }
                    return CombatAttackType.KNOCKBACK_ATTACK;
                } else {
                    return CombatAttackType.STRONG_ATTACK;
                }
            } else {
                return CombatAttackType.WEAK_ATTACK;
            }
        }
        return CombatAttackType.OTHER;
    }

}
