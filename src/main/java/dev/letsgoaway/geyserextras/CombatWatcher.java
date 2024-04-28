package dev.letsgoaway.geyserextras;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatWatcher {

    public static Map<UUID, CombatData> playersWatching = new ConcurrentHashMap<>();

    public static void watch(Player player) {
        remove(player);
        playersWatching.put(player.getUniqueId(), new CombatData(player));
    }

    public static void update() {
        playersWatching.forEach((uuid, combatData)->{
            Player player = Bukkit.getPlayer(uuid);
            if (player != null){
                playersWatching.get(uuid).update(player);
            }
            else {
                playersWatching.remove(uuid);
            }
        });
        Bukkit.getOnlinePlayers().forEach((player)->{
            if (!playersWatching.containsKey(player.getUniqueId())){
                playersWatching.put(player.getUniqueId(), new CombatData(player));
            }
        });
    }
    public static void runOnNextTick(Runnable function) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GeyserExtras.plugin, function, 1L);
    }

    public static void onAttack(Player player, EntityDamageByEntityEvent ev) {
        CombatAttackType combatAttackType = playersWatching.get(player.getUniqueId()).onAttack(player, ev);
        switch (combatAttackType) {
            case WEAK_ATTACK ->
                    player.getWorld().playSound(player.getLocation(), "java.weak", SoundCategory.PLAYERS, 1.0f, 1);
            case CRITICAL_ATTACK ->
                    player.getWorld().playSound(player.getLocation(), "java.crit", SoundCategory.PLAYERS, 1.0f, 1);
            case KNOCKBACK_ATTACK -> {
                player.getWorld().playSound(player.getLocation(), "java.knockback", SoundCategory.PLAYERS, 1.0f, 1);
                player.getWorld().playSound(player.getLocation(), "java.strong", SoundCategory.PLAYERS, 0.5f, 1);
            }
            case STRONG_ATTACK ->
                    player.getWorld().playSound(player.getLocation(), "java.strong", SoundCategory.PLAYERS, 1.0f, 1);
            case SWEEP_ATTACK ->
                    player.getWorld().playSound(player.getLocation(), "java.sweep", SoundCategory.PLAYERS, 1.0f, 1);
        }

    }



    public static void remove(Player player) {
        playersWatching.remove(player.getUniqueId());
    }
}
