package dev.letsgoaway.geyserextras.extension;

import dev.letsgoaway.geyserextras.InitializeLogger;
import dev.letsgoaway.geyserextras.PluginVersion;
import dev.letsgoaway.geyserextras.ServerType;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;

public class GeyserExtrasExtension implements Extension {
    public GeyserExtrasExtension() {
        ServerType.type = ServerType.EXTENSION;
    }

    @Subscribe
    public void onPreInitialize(GeyserPreInitializeEvent event) {

    }
}
