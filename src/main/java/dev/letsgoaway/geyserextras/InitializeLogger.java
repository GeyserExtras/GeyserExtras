package dev.letsgoaway.geyserextras;

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
    }

    public static void end() {
        DecimalFormat r3 = new DecimalFormat("0.000");
        Instant finish = Instant.now();
        info("Done! (" + r3.format(Duration.between(start, finish).toMillis() / 1000.0d) + "s)");
        info("----------------------------------------");
    }

    public static void endNoDone() {
        info("----------------------------------------");
    }

    public static void logTask(String start, Runnable task, String end){
        info(start);
        task.run();
        info(end);
    }
    public static void logTask(Runnable task, String onComplete){
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
