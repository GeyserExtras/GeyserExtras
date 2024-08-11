package dev.letsgoaway.geyserextras.core.parity.java.shield.interactions;

import org.geysermc.geyser.session.GeyserSession;

public class WalkingCheck implements Interaction {
    @Override
    public boolean check(GeyserSession session) {
        if (session.isSprinting() && !session.isSneaking()) {
            return false;
        }
        return true;
    }
}
