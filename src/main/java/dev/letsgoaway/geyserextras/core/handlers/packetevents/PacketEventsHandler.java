package dev.letsgoaway.geyserextras.core.handlers.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

public class PacketEventsHandler {
    public static void register() {
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(false);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketSendHandler(),
                PacketListenerPriority.HIGHEST);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketReceiveHandler(),
                PacketListenerPriority.HIGHEST);
        PacketEvents.getAPI().init();
    }
}
