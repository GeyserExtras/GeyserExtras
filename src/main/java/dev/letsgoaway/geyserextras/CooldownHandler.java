package dev.letsgoaway.geyserextras;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CooldownHandler {
    static String[] crosshair = {"\uF821", "\uF810", "\uF811", "\uF812", "\uF813", "\uF814", "\uF815", "\uF816", "\uF817", "\uF818", "\uF819", "\uF81A", "\uF81B", "\uF81C", "\uF81D", "\uF81E", "\uF81F"};
    static String[] hotbar = {"\uF800", "\uF801", "\uF802", "\uF803", "\uF804", "\uF805", "\uF806", "\uF807", "\uF808", "\uF809", "\uF80A", "\uF80B", "\uF80C", "\uF80D", "\uF80E", "\uF80F"};
    static List<String> specialChars = Arrays.asList("\uF820", "\uF8FF");

    // this is really bad and probably way too long atm, but hey it works
    public static void updateForPlayer(BedrockPlayer bplayer) {
        Player player = bplayer.player;
        String[] cooldownArray = hotbar;
        if (bplayer.cooldownType.equals("Crosshair")) {
            cooldownArray = crosshair;
        }
        int max = (cooldownArray.length - 1);
        int cooldown;
        if (CombatData.getCooldownPeriod(player) >= Tick.secondsToTicks(bplayer.averagePing)) {
            cooldown = (int) Math.round(Math.floor((CombatData.getCooledAttackStrength(Tick.secondsToTicks(bplayer.averagePing / 1000.0f), player)) * max));
        } else {
            cooldown = (int) Math.round(Math.floor((CombatData.getCooledAttackStrength(0.5f, player)) * max));
        }
        if (cooldown > max) {
            cooldown = max;
        }
        int deltaCoolDown = cooldown - bplayer.lastCooldown;
        if (deltaCoolDown == 0 && !bplayer.lookingAtEntity && !bplayer.lastCooldownText.equals("\uF820")) {
            return;
        }
        String textToSend = "";
        int stayTime = 5;
        if (player.getAttackCooldown() > bplayer.coolDownThresHold && bplayer.lastCooldown != max) {
            if (cooldown == max) {
                stayTime = 1;
            }
            textToSend = cooldownArray[cooldown];
        } else {
            if (bplayer.lookingAtEntity && bplayer.isTool() && bplayer.cooldownType.equals("Crosshair")) {
                textToSend = "\uF820";
                stayTime = Integer.MAX_VALUE;
            } else {
                if (bplayer.lastCooldownText.equals("\uF820")) {
                    textToSend = "\uE0AF";
                    stayTime = 1;
                    player.resetTitle();
                }
            }
        }
        if (!bplayer.lastCooldownText.equals(textToSend) && (Arrays.asList(cooldownArray).contains(bplayer.lastCooldownText) || specialChars.contains(textToSend))) {
            if (bplayer.cooldownType.equals("Crosshair")) {
                player.sendTitle(" ", textToSend, 0, stayTime, 0);
            } else {
                if (bplayer.hotbarTask != null && !bplayer.hotbarTask.isCancelled()) {
                    bplayer.hotbarTask.cancel();
                }
                String hotbarText = textToSend;
                if (bplayer.spaceHotbar) {
                    hotbarText += "\n\n";
                    for (Enchantment enchantment : player.getInventory().getItemInMainHand().getEnchantments().keySet()) {
                        if (!enchantment.equals(Enchantment.SWEEPING_EDGE)) {
                            hotbarText += "\n";
                        }
                    }
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hotbarText));
                if (stayTime != Integer.MAX_VALUE) {
                    bplayer.hotbarTask = Bukkit.getScheduler().runTaskLater(GeyserExtras.plugin,
                            () -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\uE0AF")), stayTime);
                }
            }
        }

        bplayer.lastCooldownText = textToSend;
        bplayer.lastCooldown = cooldown;
    }
}
