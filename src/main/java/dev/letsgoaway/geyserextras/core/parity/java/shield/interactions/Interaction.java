package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions;

import org.geysermc.geyser.session.GeyserSession;

public interface Interaction {
    boolean check(GeyserSession session);
}
