package dev.letsgoaway.geyserextras.core;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.registry.BlockRegistries;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.registry.type.SoundMapping;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.util.SoundUtils;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.HashMap;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public enum SoundReplacer {
    SWEEP_ATTACK("entity.player.attack.sweep", "geyserext.player.attack.sweep"),
    CRIT_ATTACK("entity.player.attack.crit", "geyserext.player.attack.crit"),
    WEAK_ATTACK("entity.player.attack.weak", "game.player.attack.nodamage"),
    KNOCKBACK_ATTACK("entity.player.attack.knockback", "geyserext.player.attack.knockback");

    public final String javaSound;
    public final String bedrockSound;
    public static HashMap<String, String> soundMap;

    SoundReplacer(String javaSound, String bedrockSound) {
        this.javaSound = javaSound;
        this.bedrockSound = bedrockSound;
    }

    public static void loadSoundMappings() {
        if (soundMap == null) {
            soundMap = new HashMap<>();
        }
        soundMap.clear();
        for (SoundReplacer sounds : SoundReplacer.values()) {
            soundMap.put(sounds.javaSound, sounds.bedrockSound);
        }
    }

    public static String getSound(String javaSound) {
        return soundMap.getOrDefault(javaSound, javaSound);
    }
}
