package dev.letsgoaway.geyserextras;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

public class InitializeLogger {
    private Consumer<String> logWarn;
    private Consumer<String> logInfo;

    public InitializeLogger(Consumer<String> warn, Consumer<String> info) {
        this.logWarn = warn;
        this.logInfo = info;
    }

    private Instant start;

    public void start() {
        start = Instant.now();
        info("--------------GeyserExtras--------------");
        info("Version: " + PluginVersion.GE_VERSION);
        info("Server Type: " + ServerType.get());
    }

    public void end() {
        DecimalFormat r3 = new DecimalFormat("0.000");
        Instant finish = Instant.now();
        info("Done! (" + r3.format(Duration.between(start, finish).toMillis() / 1000.0d) + "s)");
        info("----------------------------------------");
    }

    public void endNoDone() {
        info("----------------------------------------");
    }

    public void logTask(String start, Runnable task, String end){
        info(start);
        task.run();
        info(end);
    }
    public void logTask(Runnable task, String onComplete){
        task.run();
        info(onComplete);
    }

    public void warn(String s) {
        this.logWarn.accept(s);
    }

    public void info(String s) {
        this.logInfo.accept(s);
    }
}
