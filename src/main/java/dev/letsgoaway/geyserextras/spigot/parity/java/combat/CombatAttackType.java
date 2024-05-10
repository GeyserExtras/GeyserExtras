package dev.letsgoaway.geyserextras.spigot.parity.java.combat;

import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.GeyserExtras;
import dev.letsgoaway.geyserextras.spigot.Tick;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;

public enum CombatAttackType {
    SWEEP_ATTACK,
    CRITICAL_ATTACK,
    KNOCKBACK_ATTACK,
    STRONG_ATTACK,
    WEAK_ATTACK,
    OTHER;
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

    public static CombatAttackType getAttackType(Player player, EntityDamageByEntityEvent ev){
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
                    if (Config.knockbackAttackSprintFix && GeyserExtras.bedrockAPI.isBedrockPlayer(player.getUniqueId())) {
                        player.setSprinting(false);
                        player.sendHealthUpdate(player.getHealth(), 0, player.getSaturation());
                        Tick.runOnNext(() -> {
                            player.setSprinting(false);
                            Tick.runOnNext(() -> {
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
