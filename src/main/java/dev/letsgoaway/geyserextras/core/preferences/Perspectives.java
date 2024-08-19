package dev.letsgoaway.geyserextras.core.preferences;

import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import org.geysermc.geyser.api.bedrock.camera.CameraPerspective;
import org.geysermc.geyser.session.GeyserSession;

import java.util.LinkedHashMap;

public enum Perspectives {
    OFF,
    FIRST_PERSON,
    THIRD_PERSON_BACK,
    THIRD_PERSON_FRONT;

    public static String translate(Perspectives perspective) {
        switch (perspective) {
            case OFF -> {
                return BedrockLocale.OPTIONS.OFF;
            }
            case FIRST_PERSON -> {
                return BedrockLocale.OPTIONS.FIRST_PERSON;
            }
            case THIRD_PERSON_BACK -> {
                return BedrockLocale.OPTIONS.THIRD_PERSON_BACK;
            }
            case THIRD_PERSON_FRONT -> {
                return BedrockLocale.OPTIONS.THIRD_PERSON_FRONT;
            }
        }
        return perspective.name();
    }

    public CameraPerspective getGeyser() {
        switch (this) {
            case OFF -> {
                return null;
            }
            case FIRST_PERSON -> {
                return CameraPerspective.FIRST_PERSON;
            }
            case THIRD_PERSON_BACK -> {
                return CameraPerspective.THIRD_PERSON;
            }
            case THIRD_PERSON_FRONT -> {
                return CameraPerspective.THIRD_PERSON_FRONT;
            }
        }
        return CameraPerspective.FIRST_PERSON;
    }


    public static LinkedHashMap<String, Perspectives> buildTranslations(GeyserSession session) {
        LinkedHashMap<String, Perspectives> translations = new LinkedHashMap<>();
        for (Perspectives perspective : Perspectives.values()) {
            translations.put(translate(perspective), perspective);
        }
        return translations;
    }
}
