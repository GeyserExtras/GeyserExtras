package dev.letsgoaway.geyserextras.core.locale;

// Strings used for menus in forms from Bedrocks Locale
// No specific naming scheme really, just what im comfortable with
// but they should all be CAPITAL_SNAKE_CASE
public class BedrockLocale {
    public static final String SHOW_COORDINATES = "%createWorldScreen.showCoordinates";
    public static final String CROSSHAIR = "%options.interactionmodel.crosshair";
    public static final String SETTINGS = "%menu.settings";

    // Good enough translation for reconnecting
    public static final String RELOAD = "%profileScreen.reload";

    public static final String GAME_SETTINGS_SCREEN = "%accessibility.screenName.settings";

    public static class OPTIONS {
        // Shouldn't rely on this one being in the locales for too long because its a dev one
        public static final String DISABLED = "%options.dev_nethernet_logging_verbosity.disabled";
        public static final String DEBUG = "%options.debug";
        public static final String OFF = "%options.off";
        public static final String HIDE_PAPER_DOLL = "%options.hidepaperdoll";
        public static final String CAMERA_PERSPECTIVE = "%options.thirdperson";
        public static final String FIRST_PERSON = "%options.thirdperson.firstperson";
        public static final String THIRD_PERSON_BACK = "%options.thirdperson.thirdpersonback";
        public static final String THIRD_PERSON_FRONT = "%options.thirdperson.thirdpersonfront";

        public static final String CREDITS = "%options.credits";

    }

    public static class CONTROLLER {
        public static final String CANCEL = "%controllerLayoutScreen.cancel";
        public static final String DROP = "%controller.buttonTip.drop";

        public static final String BINDINGS = "%controllerLayoutScreen.bindings";

        public static final String RESET_TO_DEFAULT = "%controllerLayoutScreen.resetAllBindings";
    }

    public static class GUI {
        public static final String DEFAULT = "%gui.default";
        public static final String CUSTOM = "%gui.custom";

        public static final String STATS = "%gui.stats";
    }

    public static class MENU {
        public static final String RESOURCE_PACKS = "%menu.resourcepacks";
    }

    public static class KEY {
        public static final String PICK_BLOCK = "%key.pickItem";

        public static final String PLAYER_LIST = "%key.playerlist";

        public static final String INVENTORY = "%key.inventory";

        public static final String OPEN_INVENTORY = "%key.script_open_inventory";
    }
}
