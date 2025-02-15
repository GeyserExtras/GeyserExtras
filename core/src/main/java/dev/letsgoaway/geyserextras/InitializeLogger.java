package dev.letsgoaway.geyserextras;

import dev.letsgoaway.geyserextras.core.parity.java.blockdisplay.BlockDisplayEntity;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import dev.letsgoaway.geyserextras.core.version.PluginVersion;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class InitializeLogger {

    private static Instant start;

    public static void start() {
        start = Instant.now();
        info("--------------GeyserExtras--------------");
        info("Version: " + PluginVersion.GE_VERSION);
        info("Server Type: " + ServerType.get());
        info("Platform Type: " + ServerType.platform().platformName());
        if (ServerType.type != ServerType.BUNGEECORD && ServerType.type != ServerType.EXTENSION) {
            info("Floodgate installed: " + (IsAvailable.floodgate() ? "Yes" : "No"));
        }
    }

    public static void end() {
        if (GE.getConfig().isEnableBlockDisplayWorkaround()) {
            BlockDisplayEntity.buildEntityDef();
        }
        DecimalFormat r3 = new DecimalFormat("0.000");
        Instant finish = Instant.now();
        info("Done! (" + r3.format(Duration.between(start, finish).toMillis() / 1000.0d) + "s)");
        if (ServerType.type != ServerType.STANDALONE)
            info("----------------------------------------");
        else
            info("-----------------------------------------------");

        SERVER.onGeyserExtrasInitialize();
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

    private static void info(String s) {
        SERVER.log(s);
    }
}
