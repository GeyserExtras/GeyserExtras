package dev.letsgoaway.geyserextras;

public enum PlayerInputType {
    UNKNOWN("Unknown"),
    KEYBOARD_MOUSE("Keyboard"),
    TOUCH("Touch"),
    CONTROLLER("Controller"),
    VR("VR");

    public final String displayName;
    PlayerInputType(String displayName) {
        this.displayName = displayName;
    }
    public static PlayerInputType getPlayerInputType(BedrockPlayer bedrockPlayer) {
        PlayerInputType type = GeyserExtras.bedrockAPI.getPlayerInputType(bedrockPlayer);
        // if api returns unknown, guess what it likely is
        if (type.equals(UNKNOWN)) {
            return switch (PlayerPlatform.getPlayerPlatform(bedrockPlayer)) {
                case CONSOLE -> PlayerInputType.CONTROLLER;
                case MOBILE -> PlayerInputType.TOUCH;
                case PC -> PlayerInputType.KEYBOARD_MOUSE;
                case VR -> PlayerInputType.VR;
                case UNKNOWN -> PlayerInputType.UNKNOWN;
            };
        }
        return type;
    }
}
