package dev.letsgoaway.geyserextras.core;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.protocol.sound.StaticSound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public enum SoundReplacer {
    SWEEP_ATTACK("minecraft:entity.player.attack.sweep", "geyserext.player.attack.sweep"),
    CRIT_ATTACK("minecraft:entity.player.attack.crit", "geyserext.player.attack.crit"),
    WEAK_ATTACK("minecraft:entity.player.attack.weak", "game.player.attack.nodamage"),
    KNOCKBACK_ATTACK("minecraft:entity.player.attack.knockback", "geyserext.player.attack.knockback");

    public final String javaSound;
    public final String bedrockSound;
    public static final HashMap<String, Sound> soundMap = new HashMap<>();

    SoundReplacer(String javaSound, String bedrockSound) {
        this.javaSound = javaSound;
        this.bedrockSound = bedrockSound;
    }
    public static void loadSoundMappings() {
        soundMap.clear();
        for (SoundReplacer sounds : SoundReplacer.values()) {
            soundMap.put(sounds.javaSound, new StaticSound(ResourceLocation.minecraft(sounds.bedrockSound),null));
        }
    }

    public static Sound getSound(String javaSound) {
        return soundMap.getOrDefault(javaSound, Sounds.getByName(javaSound));
    }
}
