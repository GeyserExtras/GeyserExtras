package dev.letsgoaway.geyserextras;

import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import org.geysermc.geyser.api.util.PlatformType;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class InitializeLogger {

    private static Instant start;

    public static void start() {
        start = Instant.now();
        info("--------------GeyserExtras--------------");
        info("Version: " + PluginVersion.GE_VERSION);
        info("Server Type: " + ServerType.get());
        info("Platform Type: " + ServerType.platform());
        info("Floodgate installed: " + (IsAvailable.floodgate() ? "Yes" : "No"));
        if (IsAvailable.floodgate()) {
            SERVER.warn("WARNING: Floodgate is installed, so GeyserExtras settings will not");
            SERVER.warn("show up in the Game Settings menu due to how forms work on GeyserMC.");
            SERVER.warn("If you want a temporary work around to this, use Geyser-Standalone,");
            SERVER.warn("otherwise a notification toast will show up informing players that");
            SERVER.warn("they will have to double tap inventory.");
        }
    }

    public static void end() {
        DecimalFormat r3 = new DecimalFormat("0.000");
        Instant finish = Instant.now();
        info("Done! (" + r3.format(Duration.between(start, finish).toMillis() / 1000.0d) + "s)");
        if (!ServerType.isExtension())
            info("----------------------------------------");
        else
            info("-----------------------------------------------");
    }

    public static void endNoDone() {
        info("----------------------------------------");
    }

    public static void logTask(String start, Runnable task, String end) {
        info(start);
        task.run();
        info(end);
    }

    public static void logTask(Runnable task, String onComplete) {
        task.run();
        info(onComplete);
    }

    private static void warn(String s) {
        SERVER.warn(s);
    }

    private static void info(String s) {
        SERVER.log(s);
    }
}
