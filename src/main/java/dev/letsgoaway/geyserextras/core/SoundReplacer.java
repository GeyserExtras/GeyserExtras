package dev.letsgoaway.geyserextras.core;

import java.util.HashMap;

public enum SoundReplacer {
    SWEEP_ATTACK("minecraft:entity.player.attack.sweep", "geyserext.player.attack.sweep"),
    CRIT_ATTACK("minecraft:entity.player.attack.crit", "geyserext.player.attack.crit"),
    STRONG_ATTACK("minecraft:entity.player.attack.strong", "game.player.attack.strong"),
    WEAK_ATTACK("minecraft:entity.player.attack.weak", "game.player.attack.nodamage"),
    KNOCKBACK_ATTACK("minecraft:entity.player.attack.knockback", "geyserext.player.attack.knockback");

    public final String javaSound;
    public final String bedrockSound;
    public static HashMap<String, String> soundMap = new HashMap<>();

    SoundReplacer(String javaSound, String bedrockSound) {
        this.javaSound = javaSound;
        this.bedrockSound = bedrockSound;
    }

    public static void loadSoundMappings() {
        soundMap.clear();
        for (SoundReplacer sounds : SoundReplacer.values()) {
            soundMap.put(sounds.javaSound, sounds.bedrockSound);
        }
    }

    public static String replaceSound(String javaSound) {
        return soundMap.getOrDefault(javaSound, javaSound);
    }
}
