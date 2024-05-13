package dev.letsgoaway.geyserextras.bungee;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.EventRegistrar;

import java.nio.charset.StandardCharsets;

public class GeyserEventForwarder implements EventRegistrar {
    public static final org.geysermc.geyser.api.GeyserApi api = org.geysermc.geyser.api.GeyserApi.api();

    public GeyserEventForwarder() {
        GeyserExtras.initLog.logTask("Registering events...", this::tryRegisterEventBus, "Events registered!");
    }

    public static boolean enableNetherFog = true;

    private void tryRegisterEventBus() {
        api.eventBus().subscribe(this, org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent.class, this::onClientEmoteEvent);
    }

    @Subscribe
    public void onClientEmoteEvent(org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent ev) {
        GeyserExtras.proxyServer.getPlayer(ev.connection().javaUuid()).sendData(GeyserExtras.emoteChannel, ev.emoteId().getBytes(StandardCharsets.UTF_8));
    }
}
