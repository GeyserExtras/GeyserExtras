package dev.letsgoaway.geyserextras.spigot.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
@Getter

// TODO make wiki page/s to explain stuff better
public final class GeyserExtrasSpigotConfig {
    @Comment("""
            Forces block placements to not be hold-bridgable/scaffold-bridgeable.
            Requires 'disable-bedrock-scaffolding: true' in Geyser's config for forward block placements.
            """)
    private boolean enableJavaOnlyBlockPlacement = false;

    @Comment("Updates the blocks at the player's line of sight every tick to remove ghost blocks.")
    private boolean enableBlockGhostingWorkaround = false;

   /*
    @Comment("""
            Enables the Knockback Attack Sprint Workaround.
            In Java Edition, when you execute a Knockback Attack, the player stops sprinting for one tick.
            GeyserExtras works around this by tricking the Bedrock Edition client into having no hunger
            for one game tick, however this temporarily makes the hunger bar appear empty.
            """)
    private boolean enableKnockbackAttackSprintWorkaround = false;
    */

    /*
 @Comment("""
         Plays the Java Edition crop break and place sound for Bedrock Edition Players.
         """)
 private boolean enableCropSoundWorkaround = true;
*/
    @Comment("The version of the config. DO NOT CHANGE!")
    private int version = SpigotConfigLoader.LATEST_VERSION;
}
