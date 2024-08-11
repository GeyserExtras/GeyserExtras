package dev.letsgoaway.geyserextras.core.parity.java;

import dev.letsgoaway.geyserextras.MathUtils;
import dev.letsgoaway.geyserextras.ReflectionAPI;
import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.entity.attribute.GeyserAttributeType;
import org.geysermc.geyser.session.GeyserSession;


public class CooldownHandler {
    private final ExtrasPlayer player;

    private GeyserSession session;

    @Setter
    private long lastSwingTime;

    @Setter
    @Getter
    public double attackSpeed = 4.0;


    /**
     * -1 means the player is not digging
     */
    @Setter
    @Getter
    public int digTicks = -1;

    @Setter
    private long lastMouseoverID = 0;

    // Shield stuff
    @Setter
    @Getter
    private boolean skipNextItemUse1 = false;

    @Setter
    @Getter
    private long lastBlockRightClickTime = 0;

    @Setter
    @Getter
    private boolean lastClickWasAirClick = false;


    public CooldownHandler(ExtrasPlayer player) {
        this.player = player;
        lastSwingTime = System.currentTimeMillis();
        session = player.getSession();
    }

    public boolean readyToAttack = false;

    public void tick() {
        if (Config.toggleBlock) {
            setArmAnimationTicks(-1);
        }
        if (lastMouseoverID != 0 && session.getMouseoverEntity() != null && player.isTool()) {
            readyToAttack = session.getMouseoverEntity().isAlive();
        } else {
            readyToAttack = false;
        }
        double time = (System.currentTimeMillis() - averagePing) - lastSwingTime;
        double cooldown = MathUtils.restrain((time) * attackSpeed / 1000.0, 1);
        sendCooldown(cooldown);
    }

    private static final String[] crosshair = {"\uF821", "\uF810", "\uF811", "\uF812", "\uF813", "\uF814", "\uF815", "\uF816", "\uF817", "\uF818", "\uF819", "\uF81A", "\uF81B", "\uF81C", "\uF81D", "\uF81E", "\uF81F"};
    private static final String[] hotbar = {"\uF800", "\uF801", "\uF802", "\uF803", "\uF804", "\uF805", "\uF806", "\uF807", "\uF808", "\uF809", "\uF80A", "\uF80B", "\uF80C", "\uF80D", "\uF80E", "\uF80F"};

    private static final String crosshairAttackReady = "\uF820";

    private String lastCharSent = "";

    private void sendCooldown(double progress) {
        if (digTicks != -1 || progress == 1.0) {
            switch (session.getPreferencesCache().getCooldownPreference()) {
                case TITLE -> {
                    if (readyToAttack && !lastCharSent.equals(crosshairAttackReady)) {
                        lastCharSent = crosshairAttackReady;
                        player.sendTitle("", lastCharSent, 0, Integer.MAX_VALUE, 0);
                    } else if (!readyToAttack && !lastCharSent.isEmpty()) {
                        lastCharSent = "";
                        player.resetTitle();
                    }
                }
                case ACTIONBAR -> {
                }
                case DISABLED -> {
                }
            }
            return;
        }
        switch (session.getPreferencesCache().getCooldownPreference()) {
            case TITLE -> {
                int max = (crosshair.length - 1);
                // java math is so good i love it alot
                int cooldown = Math.toIntExact(Math.round(Math.floor(progress * max)));
                if (cooldown > max) {
                    cooldown = max;
                }
                String curChar = crosshair[cooldown];
                if (lastCharSent.equals(curChar)) {
                    return;
                }
                lastCharSent = curChar;
                player.sendTitle("", lastCharSent, 0, MathUtils.ceil((float) getCooldownPeriod()), 0);
            }
            case ACTIONBAR -> {
            }
            case DISABLED -> {
            }
        }
    }

    public double getCooldownPeriod() {
        return 1.0D / attackSpeed * 20.0;
    }

    private double averagePing = 0.0f;
    private long pingSample = 0;

    private long pingSampleSize = 0;

    private int lastPing = -1;

    private void calculateAveragePing() {
        int ping = session.ping();
        if (ping != lastPing) {
            pingSample += ping;
            pingSampleSize++;
            lastPing = ping;
        }
        averagePing = (double) pingSample / pingSampleSize;
    }
    // Used to disable the automatic re blocking when sneaking + attacking done by Geyser
    private void setArmAnimationTicks(int ticks) {
        try {
            ReflectionAPI.setValue(session, ReflectionAPI.getFieldAccessible(GeyserSession.class, "armAnimationTicks"), ticks);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
