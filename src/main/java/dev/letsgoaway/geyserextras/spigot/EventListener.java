package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.spigot.parity.java.combat.CombatAttackType;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

public class EventListener implements Listener {
    public boolean notBedrock(Player player) {
        return !GeyserExtras.bplayers.containsKey(player.getUniqueId());
    }

    public BedrockPlayer getPlayer(Player player) {
        return GeyserExtras.bplayers.getOrDefault(player.getUniqueId(), null);
    }

    public static void stopSoundForBlock(String blockName, BedrockPlayer bplayer) {
        bplayer.player.stopSound("minecraft:dig." + blockName);
        bplayer.player.stopSound("minecraft:use." + blockName);
        bplayer.player.stopSound("minecraft:step." + blockName);
        bplayer.player.stopSound("minecraft:land." + blockName);
        bplayer.player.stopSound("minecraft:jump." + blockName);
        bplayer.player.stopSound("minecraft:hit." + blockName);
        bplayer.player.stopSound("minecraft:fall." + blockName);
    }

    public static void cropStop(BedrockPlayer bplayer) {
        stopSoundForBlock("grass", bplayer);
        stopSoundForBlock("gravel", bplayer);
        stopSoundForBlock("dirt", bplayer);
        bplayer.player.stopSound("minecraft:block.crop.break");
        bplayer.player.stopSound("minecraft:item.crop.plant");
    }

    private void stopGrassSounds(BedrockPlayer bplayer) {
        cropStop(bplayer);
        Tick.runIn(2L, () -> {
            cropStop(bplayer);
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        if (BedrockPlayer.cropTypes.contains(ev.getMaterial())) {
            for (BedrockPlayer bplayer : GeyserExtras.bplayers.values()) {
                if (player.getWorld().equals(bplayer.player.getWorld()) && 48.0 >= Math.abs(bplayer.player.getLocation().distance(player.getLocation()))) {
                    stopGrassSounds(bplayer);
                }
            }
        }
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerInteract(ev);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent ev) {
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerInteractEntity(ev);
    }


    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent ev) {
        if (ev.getDamager() instanceof Player player) {
            if (Config.javaCombatSounds && ev.getEntity().getType().isAlive()) {
                CombatAttackType combatAttackType = CombatAttackType.getAttackType(player, ev);
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
            if (notBedrock(player)) {
                return;
            }
            getPlayer(player).onPlayerDamageEntity(ev);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent ev) {
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerMoveEvent(ev);
    }

    @EventHandler
    public void onPlayerItemHeldSwitch(PlayerItemHeldEvent ev) {
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerItemHeldSwitch(ev);
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent ev) {
        Player player = ev.getPlayer();
        if (BedrockPlayer.cropTypes.contains(ev.getBlock().getType())) {
            player.getWorld().playSound(ev.getBlockPlaced().getLocation(),
                    "plant.crop", SoundCategory.BLOCKS, 0.85f, BedrockPlayer.random.nextBoolean() ? 1.0f : 1.2f);
            for (BedrockPlayer bplayer : GeyserExtras.bplayers.values()) {
                if (player.getWorld().equals(bplayer.player.getWorld()) && 48.0 >= Math.abs(bplayer.player.getLocation().distance(player.getLocation()))) {
                    stopGrassSounds(bplayer);
                }
            }
        }
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerBlockPlace(ev);
    }

    @EventHandler
    public void onPlayerBlockDamage(BlockDamageEvent ev) {
        Player player = ev.getPlayer();
        for (BedrockPlayer bplayer : GeyserExtras.bplayers.values()) {
            if (player.getWorld().equals(bplayer.player.getWorld()) && 48.0 >= Math.abs(bplayer.player.getLocation().distance(player.getLocation()))) {
                if (ev.getInstaBreak()) {
                    stopSoundForBlock("stone", bplayer);
                }
            }
        }
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerBlockDamage(ev);
    }

    @EventHandler
    public void onPlayerBlockDamageAbort(BlockDamageAbortEvent ev) {
        Player player = ev.getPlayer();

        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerBlockDamageAbort(ev);
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent ev) {
        Player player = ev.getPlayer();
        if (BedrockPlayer.cropTypes.contains(ev.getBlock().getType())) {
            // For some reason bedrock has the sound that is meant to play but doesnt play it for crops
            // "we are always working on parity!" - mojang
            player.getWorld().playSound(ev.getBlock().getLocation(), "block.bamboo_sapling.break", SoundCategory.BLOCKS, 0.9f, 1.0f);
            for (BedrockPlayer bplayer : GeyserExtras.bplayers.values()) {
                if (player.getWorld().equals(bplayer.player.getWorld()) && 48.0 >= Math.abs(bplayer.player.getLocation().distance(player.getLocation()))) {
                    if (BedrockPlayer.cropTypes.contains(ev.getBlock().getType())) {
                        stopGrassSounds(bplayer);
                    }
                }
            }
        }
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerBlockBreak(ev);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent ev) {
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
        getPlayer(player).onPlayerDrop(ev);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
    }

    @EventHandler
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent ev) {
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
        BedrockPlayer bedrockPlayer = getPlayer(player);

        bedrockPlayer.onPlayerChangeWorlds(ev);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent ev) {
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
        BedrockPlayer bedrockPlayer = getPlayer(player);
        bedrockPlayer.onPlayerLeave(ev);
        GeyserExtras.bplayers.remove(ev.getPlayer().getUniqueId());
    }

    public void runOnNextTick(Runnable function) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GeyserExtras.plugin, function, 1L);
    }

    @EventHandler
    public void onExecuteCommand(PlayerCommandPreprocessEvent ev) {
        if ((ev.getMessage().equals("/geyser reload") || ev.getMessage().equals("/stop")) && Config.autoReconnect) {
            ev.setCancelled(true);
            for (BedrockPlayer bedrockPlayer : GeyserExtras.bplayers.values()) {
                GeyserExtras.bedrockAPI.reconnect(bedrockPlayer.player.getUniqueId());
            }
            runOnNextTick(() -> {
                ev.getPlayer().performCommand(ev.getMessage().replace("/", ""));
            });
        }
        Player player = ev.getPlayer();
        if (notBedrock(player)) {
            return;
        }
        if (ev.getMessage().equalsIgnoreCase("/geyser extras")) {
            ev.setCancelled(true);
            getPlayer(player).onGeyserExtrasCommand();
        }
    }
}
