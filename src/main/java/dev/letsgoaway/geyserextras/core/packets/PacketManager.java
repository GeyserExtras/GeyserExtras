package dev.letsgoaway.geyserextras.core.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

public class PacketManager {

    public PacketManager(PacketEventsAPI<?> api) {
        PacketEvents.setAPI(api);
    }

    public void init() {
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(false);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketHandler(),
                PacketListenerPriority.HIGH);
    }
}