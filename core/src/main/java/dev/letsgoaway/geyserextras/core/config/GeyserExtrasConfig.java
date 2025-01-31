package dev.letsgoaway.geyserextras.core.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
@Getter
public final class GeyserExtrasConfig {
    @Comment("""
            Enable the Java Edition styled cooldown.""")
    private boolean enableCustomCooldown = true;

    @Comment("Enable the Java Edition combat sounds.")
    private boolean enableJavaCombatSounds = true;

    @Comment("""
            Forces block placements to not be hold-bridgable/scaffold-bridgeable.
            Requires disable-bedrock-scaffolding: true in Geyser's config for forward block placements.
            """)
    private boolean enableJavaOnlyBlockPlacement = false;

    @Comment("Updates the block at the player's line of sight every tick to remove ghost blocks.")
    private boolean enableBlockGhostingFix = false;

    @Comment("When the server closes or Geyser reloads, should GeyserExtras reconnect to the server using the below address.")
    private boolean autoReconnect = true;

    @Comment("""
            The actions that will show up in the Quick-Menu. Quick-Menu requires the plugin version of Geyser.
            Actions are formatted as shown below,
            "{title} >> {command}"
            Commands are executed as the player.
            Available placeholders are:
                        
            %player_name% - The name that is the Java Username of the player. For example: .Geyser_Extras
            If the player is linked the linked Java account's name is used.
                        
            %xbox_username% - The username that the Bedrock player has. For example: Geyser Extras
                        
            %player_device% - The device the player is using currently.
            Possible values: Android | iOS | Amazon | Windows Phone | Gear VR | Hololens | Windows | macOS | Apple TV | PlayStation | Switch | Xbox | Dedicated | Unknown
                        
            %player_platform% - The platform the player is using currently.
            Possible values: Console | Mobile | PC | VR | Unknown
                        
            %player_inputtype% - The input type the player is using.
            Possible values: Keyboard | Touch | Controller | VR | Unknown
                        
            %player_uiprofile% - The UI Profile the player is using.
            Possible values: Classic | Pocket
            """)
    private List<String> quickMenuActions = new ArrayList<>(Arrays.asList(
            "Swap Items with Off-hand >> /geyser offhand",
            "Toggle Tooltips >> /geyser tooltips",
            "Open GeyserExtras Menu >> /ge",
            "Open Advancements >> /geyser advancements",
            "Open Statistics >> /geyser statistics",
            "Player List >> /playerlist",
            "Platform List >> /platformlist")
    );

    @Comment("""
            Enables the Knockback Attack Sprint fix.
            In Java, when you execute a Knockback attack, the Player stops sprinting for one tick.
            The workaround for Bedrock tricks the client into having no hunger for a tick, however
            this temporarily makes the hunger bar look empty.
            """)
    private boolean enableKnockbackAttackSprintFix = true;

    @Comment("""
            Enables downloading of Bedrock Player skins.
            This will download the Geometry, Animations, and Textures of your players skins/capes.
            Persona skins are currently bugged.
            """)
    private boolean enableSkinSaving = false;

    @Comment("""
            Sets the default emote chat option for Java Players.
            The GeyserExtras plugin forwards emote chat from Bedrock Edition to Java Players.
            Java Players can still choose if they want to see emote chat by doing /emotechat
            """)
    private boolean muteEmoteChat = false;

    @Comment("""
            Whether to disable the Paper Doll or not.
            The Paper Doll is the small player graphic in the top left of bedrock players screens
            This can give players a competitive advantage over java in some cases, for example,
            you can see what armor you have on without pressing the inventory key
            you can see what armor has broken during combat
            """)
    private boolean disablePaperDoll = false;

    @Comment("Only enable if you know what you are doing.")
    private boolean debugMode = false;

    @Comment("The version of the config. DO NOT CHANGE!")
    private int version = 1;
}
