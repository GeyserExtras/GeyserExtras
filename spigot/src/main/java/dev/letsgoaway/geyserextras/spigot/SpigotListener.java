package dev.letsgoaway.geyserextras.spigot;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.RayTraceResult;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class SpigotListener implements Listener {
    public static final List<String> cropTypes = Arrays.asList(
            "WHEAT",
            "WHEAT_SEEDS",
            "SEEDS",
            "CROPS",
            "CARROT",
            "CARROTS",
            "CARROT_ITEM",
            "POTATO",
            "POTATOES",
            "POTATO_ITEM",
            "BEETROOT",
            "BEETROOT_SEEDS",
            "BEETROOT_BLOCK",
            "BEETROOTS",
            "MELON_SEEDS",
            "MELON_STEM",
            "PUMPKIN_SEEDS",
            "PUMPKIN_STEM"
    );

    public static void stopSoundForBlock(String blockName, Player player) {
        player.stopSound("minecraft:dig." + blockName);
        player.stopSound("minecraft:use." + blockName);
        player.stopSound("minecraft:step." + blockName);
        player.stopSound("minecraft:land." + blockName);
        player.stopSound("minecraft:jump." + blockName);
        player.stopSound("minecraft:hit." + blockName);
        player.stopSound("minecraft:fall." + blockName);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        GeyserExtrasSpigot.CORE.onJavaPlayerJoin(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent ev) {
        Player player = ev.getPlayer();
        // TODO + update pack for crop sounds
        /*
        // Crop Sound Workaround (ported from v1.0)


        if (cropTypes.contains(ev.getBlock().getType().name()))
            player.getWorld().playSound(ev.getBlockPlaced().getLocation(),
                    "plant.crop", SoundCategory.BLOCKS, 0.85f, ThreadLocalRandom.current().nextBoolean() ? 1.0f : 1.2f);

        for (ExtrasPlayer otherEP : GE.connections.values()) {

            if (otherEP instanceof SpigotExtrasPlayer otherPlayer) {
                if (player.getWorld().equals(bplayer.player.getWorld()) && 48.0 >= Math.abs(bplayer.player.getLocation().distance(player.getLocation()))) {
                    stopGrassSounds(bplayer);
                }
            }
        }


         */

        // Java Only Block Placement (ported from v1.0)

        ExtrasPlayer user = GeyserHandler.getPlayer(player.getUniqueId());
        if (user instanceof SpigotExtrasPlayer extrasPlayer) {
            if (GeyserExtrasSpigot.SPIGOT.getPlatformConfig().isEnableJavaOnlyBlockPlacement()) {
                Block block = ev.getBlockPlaced();
                double interactionRange = extrasPlayer.getSession().getPlayerEntity().getBlockInteractionRange();

                if (block.getType().equals(Material.AIR) || ev.getBlockAgainst().getType().equals(Material.AIR)) {
                    ev.setCancelled(true);
                }
                else {
                    RayTraceResult blockCast = player.rayTraceBlocks(interactionRange, FluidCollisionMode.ALWAYS);

                    if (blockCast != null) {
                        ev.setCancelled(!block.equals(blockCast.getHitBlock()));
                    }
                }
                if (ev.isCancelled()) {
                    block.setType(Material.AIR);
                    player.teleport(player.getLocation());
                    player.updateInventory();
                }
            }
        }
    }

    private void cropStop(Player player) {
        stopSoundForBlock("grass", player);
        stopSoundForBlock("gravel", player);
        stopSoundForBlock("dirt", player);
        player.stopSound("minecraft:block.crop.break");
        player.stopSound("minecraft:item.crop.plant");
    }

    public void onPlayerLeave(Player player) {
        GeyserExtrasSpigot.CORE.onJavaPlayerLeave(player.getUniqueId());
    }
}
