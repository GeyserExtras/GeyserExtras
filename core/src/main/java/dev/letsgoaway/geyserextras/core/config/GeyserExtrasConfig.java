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

// TODO make wiki page/s to explain stuff better
public final class GeyserExtrasConfig {
    @Comment("""
            Enable the GeyserExtras menu, which can be opened by players by
            double-tapping inventory, or typing `/ge`.
            \s
            The GeyserExtras Menu provides actions and configurable settings such as
            - Remapping certain actions (e.g emotes/pick block) to Java Edition only actions (e.g offhand)
            - Configurable HUD Visibility
            - Quick access to form remakes of Java Edition Menus
              (e.g Player List, Advancements, Statistics).
            \s
            You can configure default options for Bedrock Players at
            `preferences/default.json`
            and for Java Players at
            `preferences/java/default.json`.
            \s
            Setting this to `false` also disables saving of preferences.
            """)
    private boolean enableGeyserExtrasMenu = true;

    @Comment("""
            Enable the Java Edition styled cooldown.""")
    private boolean enableCustomCooldown = true;

    @Comment("Enable the Java Edition combat sounds.")
    private boolean enableJavaCombatSounds = true;

    @Comment("""
    Enable the Block Display entity workaround using FMBE. (EXPERIMENTAL)
    Note that the translation is not 100% accurate and some block types might be invisible (candles, doors, campfires),
    where as some blocks will render differently (fence, glass panes, some storage containers).
    Block Display billboards do not work and X/Z scale are linked to which ever one is highest,
    meaning that this should only be enabled for very simple usages of Block Displays.
    (https://wiki.bedrock.dev/commands/display-entities.html)
    """)
    private boolean enableBlockDisplayWorkaround = false;

    @Comment("When the server closes or Geyser reloads, should GeyserExtras reconnect to the server using the below address.")
    private boolean autoReconnect = true;

    @Comment(
            """
            The language that Java Edition players read when using a GeyserExtras command (e.g, `/muteemotechat`).
            This is automatically set on Bedrock Edition from the player's settings.
            You can see the list of all locale codes here:
            https://github.com/GeyserExtras/data/blob/main/langs/language_names.json
            """)
    private String defaultLocale = "en_US";

    @Comment("""
            Enables downloading of Bedrock Player skins.
            This will download the Geometry, Animations, and Textures of your players skins/capes.
            Persona skins are currently bugged.
            """)
    private boolean enableSkinSaving = false;


    @Comment("""
            Whether to disable the Paper Doll or not.
            The Paper Doll is the small player graphic in the top left of bedrock players screens
            This can give players a competitive advantage over java in some cases, for example,
            you can see what armor you have on without pressing the inventory key
            you can see what armor has broken during combat
            """)
    private boolean disablePaperDoll = false;

    @Comment("""
    Whether to check for updates or not.
    This also disables updating of the GeyserExtrasPack and GeyserOptionalPack,
    however you can manually update them by deleting the 'GeyserExtras/cache/' folder.
    """)
    private boolean checkForUpdates = true;

    @Comment("Only enable if you know what you are doing.")
    private boolean debugMode = false;

    @Comment("The version of the config. DO NOT CHANGE!")
    private int version = ConfigLoader.LATEST_VERSION;
}
