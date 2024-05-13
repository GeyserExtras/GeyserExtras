package dev.letsgoaway.geyserextras.extension;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.ReleaseVersion;
import dev.letsgoaway.geyserextras.ServerType;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;

public class GeyserExtras implements Extension {
    InitializeLogger initLog;

    public GeyserExtras() {
        ServerType.type = ServerType.EXTENSION;
    }

    @Subscribe
    public void onPreInitialize(GeyserPreInitializeEvent event) {
        initLog = new InitializeLogger((s) -> logger().warning(s), (s) -> logger().info(s));
        initLog.start();
        initLog.warn("GeyserExtras is currently not supported as a Geyser Extension!");
        initLog.warn("If you are running Geyser on your server/proxy, place this plugin");
        initLog.warn("in your softwares's plugins folder and not the Geyser Extensions folder!");
        initLog.warn("Disabling...");
        initLog.endNoDone();
        this.setEnabled(false);
        PluginVersion.checkForUpdatesAndPrintToLog((s) -> logger().warning(s));
    }
}
