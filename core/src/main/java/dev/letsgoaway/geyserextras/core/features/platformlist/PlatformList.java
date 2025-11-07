package dev.letsgoaway.geyserextras.core.features.platformlist;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.api.util.InputMode;
import org.geysermc.api.util.BedrockPlatform;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.auth.BedrockClientData;

import java.util.UUID;

public class PlatformList {
    public enum Platform {
        UNKNOWN,
        CONSOLE,
        DESKTOP,
        MOBILE,
    }

    public enum Ecosystem {
        UNKNOWN,
        JAVA,
        WINDOWS,
        APPLE,
        ANDROID,
        AMAZON,
        PLAYSTATION,
        XBOX,
        NINTENDO,
        SERVER
    }

    public enum Device {
        UNKNOWN,
        JAVA,
        PS4,
        PS5,
        SWITCH,
        SWITCH_2,
        XBOX_ONE,
        XBOX_SERIES,
        LINUX,
        WINDOWS_PHONE,
        WINDOWS_10_PC,
        WINDOWS_11_PC,
        ANDROID_DEVICE,
        AMAZON_FIRE,
        CHROMEBOOK,
        IPHONE,
        IPAD,
        IMAC,
        APPLETV,
    }



    public Ecosystem getEcosystem(UUID uuid) {
        ExtrasPlayer player = ExtrasPlayer.get(uuid);
        if (player == null) {
            return Ecosystem.JAVA;
        }
        return getEcosystem(player.getSession());
    }

    public Ecosystem getEcosystem(GeyserConnection connection) {
        return switch (connection.platform()) {
            case UNKNOWN -> Ecosystem.UNKNOWN;
            case GOOGLE,GEARVR -> Ecosystem.ANDROID;
            case NX -> Ecosystem.NINTENDO;
            case OSX,IOS,TVOS -> Ecosystem.APPLE;
            case AMAZON -> Ecosystem.AMAZON;
            case PS4 -> Ecosystem.PLAYSTATION;
            case UWP,WIN32,WINDOWS_PHONE,HOLOLENS -> Ecosystem.WINDOWS;
            case XBOX -> Ecosystem.XBOX;
            case LINUX,DEDICATED -> Ecosystem.SERVER;
        };
    }


    public Platform getPlatform(UUID uuid) {
        ExtrasPlayer player = ExtrasPlayer.get(uuid);
        if (player == null) {
            return Platform.DESKTOP;
        }
        return getPlatform(player.getSession());
    }


    public Platform getPlatform(GeyserConnection connection) {
        return switch (connection.platform()) {
            case UNKNOWN -> Platform.UNKNOWN;
            case GOOGLE, IOS, AMAZON, WINDOWS_PHONE, GEARVR, HOLOLENS -> Platform.MOBILE;
            case OSX, UWP, WIN32, DEDICATED -> Platform.DESKTOP;
            case TVOS, PS4, NX, XBOX, LINUX -> Platform.CONSOLE;
        };
    }

    public Device getDevice(UUID uuid) {
        ExtrasPlayer player = ExtrasPlayer.get(uuid);
        if (player == null) {
            return Device.JAVA;
        }
        return getDevice(player.getSession());
    }

    public Device getDevice(GeyserSession session) {
        BedrockClientData data = session.getClientData();
        String model = data.getDeviceModel();
        switch (session.platform()) {
            case UNKNOWN,DEDICATED -> {
                return Device.UNKNOWN;
            }
            case GOOGLE -> {
                // todo: chrome book detection
                return Device.ANDROID_DEVICE;
            }
            case IOS -> {
                // todo: iphone detection
                return Device.IPAD;
            }
            case OSX -> {
                return Device.IMAC;
            }
            case AMAZON -> {
                return Device.AMAZON_FIRE;
            }
            case GEARVR -> {
                return Device.ANDROID_DEVICE;
            }
            case HOLOLENS, UWP, WIN32 -> {
                // todo: windows 11 pc detection
                return Device.WINDOWS_10_PC;
            }
            case TVOS -> {
                return Device.APPLETV;
            }
            case PS4 -> {
                // todo: PS5 detection
                return Device.PS4;
            }
            case NX -> {
                // todo: switch 2 detection

                return Device.SWITCH;
            }
            case XBOX -> {
                // todo: xbox series detection
                return Device.XBOX_ONE;
            }
            case WINDOWS_PHONE -> {
                return Device.WINDOWS_PHONE;
            }
            case LINUX -> {
                // i dont think chromebooks send linux but eh
                return Device.CHROMEBOOK;
            }
        }
        return Device.UNKNOWN;
    }

}
