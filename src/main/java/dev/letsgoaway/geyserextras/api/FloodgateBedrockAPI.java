package dev.letsgoaway.geyserextras.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.letsgoaway.geyserextras.*;
import dev.letsgoaway.geyserextras.menus.TabList;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

public class FloodgateBedrockAPI extends dev.letsgoaway.geyserextras.api.BedrockPluginAPI {
    private final org.geysermc.floodgate.api.FloodgateApi api = org.geysermc.floodgate.api.FloodgateApi.getInstance();

    public FloodgateBedrockAPI() {
        super();
        tryRegisterEventBus();
    }

    private void tryRegisterEventBus() {
        api.getEventBus().subscribe(SkinApplyEvent.class, this::onSkinApplyEvent);
    }

    @Override
    public boolean isBedrockPlayer(UUID uuid) {
        return api.isFloodgatePlayer(uuid);
    }

    @Override
    public boolean sendForm(UUID uuid, Form form) {
        return api.sendForm(uuid, form);
    }

    @Override
    public boolean sendForm(UUID uuid, FormBuilder<?, ?, ?> form) {
        return this.sendForm(uuid, form.build());
    }

    @Override
    public void onConfigLoad() {

    }

    @Subscribe
    public void onSkinApplyEvent(SkinApplyEvent ev) {
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new String(Base64.getDecoder().decode(ev.newSkin().value()), StandardCharsets.UTF_8));
        JsonObject rootobj = root.getAsJsonObject();
        TabList.bedrockPlayerTextureIDs.put(ev.player().getXuid(), rootobj.getAsJsonObject("textures")
                .getAsJsonObject("SKIN")
                .getAsJsonPrimitive("url")
                .getAsString()
                .replace("http://textures.minecraft.net/texture/","")
        );
    }

    @Override
    public void reconnect(UUID uuid) {
        api.transferPlayer(uuid, Config.externalAddress, Config.externalPort);
    }

    @Override
    public PlayerDevice getPlayerDevice(UUID uuid) {
        return switch (Objects.requireNonNull(api.getPlayer(uuid)).getDeviceOs()) {
            case GOOGLE -> PlayerDevice.ANDROID;
            case IOS -> PlayerDevice.IOS;
            case OSX -> PlayerDevice.OSX;
            case AMAZON -> PlayerDevice.AMAZON;
            case GEARVR -> PlayerDevice.GEARVR;
            case HOLOLENS -> PlayerDevice.HOLOLENS;
            case UWP, WIN32 -> PlayerDevice.WINDOWS;
            case DEDICATED -> PlayerDevice.DEDICATED;
            case TVOS -> PlayerDevice.TVOS;
            case PS4 -> PlayerDevice.PLAYSTATION;
            case NX -> PlayerDevice.SWITCH;
            case XBOX -> PlayerDevice.XBOX;
            case WINDOWS_PHONE -> PlayerDevice.WINDOWS_PHONE;
            default -> PlayerDevice.UNKNOWN;
        };
    }

    @Override
    public PlayerInputType getPlayerInputType(UUID uuid) {
        return switch (Objects.requireNonNull(api.getPlayer(uuid)).getInputMode()) {
            case CONTROLLER -> PlayerInputType.CONTROLLER;
            case TOUCH -> PlayerInputType.TOUCH;
            case KEYBOARD_MOUSE -> PlayerInputType.KEYBOARD_MOUSE;
            case VR -> PlayerInputType.VR;
            default -> PlayerInputType.UNKNOWN;
        };
    }

    @Override
    public PlayerUIProfile getPlayerUIProfile(UUID uuid) {
        return switch (Objects.requireNonNull(api.getPlayer(uuid)).getUiProfile()) {
            case CLASSIC -> PlayerUIProfile.CLASSIC;
            case POCKET -> PlayerUIProfile.POCKET;
        };
    }

    @Override
    public String getXboxUsername(UUID uuid) {
        return Objects.requireNonNull(api.getPlayer(uuid)).getUsername();
    }

    @Override
    public String getPlayerXUID(UUID uuid) {
        return Objects.requireNonNull(api.getPlayer(uuid)).getXuid();
    }

    @Override
    public boolean isLinked(UUID uuid) {
        return Objects.requireNonNull(api.getPlayer(uuid)).isLinked();
    }

    @Override
    public void sendFog(UUID uuid, String fog) {
    }

    @Override
    public void removeFog(UUID uuid, String fog) {
    }

    @Override
    public UUID getPackID(Path path) {
        return UUID.nameUUIDFromBytes(path.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getPackName(String id) {
        return "Unknown";
    }

    @Override
    public String getPackDescription(String id) {
        return "";
    }

    @Override
    public String getPackVersion(String id) {
        return "?.?.?";
    }

    @Override
    @Nullable
    public Path getPackPath(String id) {
        return null;
    }

    @Override
    public boolean getPackExists(String id) {
        return false;
    }


}
