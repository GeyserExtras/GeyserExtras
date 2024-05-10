package dev.letsgoaway.geyserextras.spigot.api;

import dev.letsgoaway.geyserextras.spigot.player.PlayerDevice;
import dev.letsgoaway.geyserextras.spigot.player.PlayerInputType;
import dev.letsgoaway.geyserextras.spigot.player.PlayerUIProfile;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.UUID;

public abstract class BedrockPluginAPI implements IBedrockPluginAPI {
    public BedrockPluginAPI() {

    }
    public abstract boolean isBedrockPlayer(UUID uuid);
    public abstract boolean sendForm(UUID uuid, Form form);
    public abstract boolean sendForm(UUID uuid, FormBuilder<?,?,?> form);

    public abstract void onConfigLoad();

    public abstract void reconnect(UUID uuid);

    public abstract PlayerDevice getPlayerDevice(UUID uuid);

    public abstract PlayerInputType getPlayerInputType(UUID uuid);

    public abstract PlayerUIProfile getPlayerUIProfile(UUID uuid);

    public abstract String getXboxUsername(UUID uuid);

    public abstract String getPlayerXUID(UUID uuid);

    public abstract boolean isLinked(UUID uuid);

    public abstract void sendFog(UUID uniqueId, String fog);

    public abstract void removeFog(UUID uuid, String fog);

    public abstract UUID getPackID(Path path);

    public abstract String getPackName(String id);

    public abstract String getPackDescription(String id);

    public abstract String getPackVersion(String id);

    @Nullable
    public abstract Path getPackPath(String id);

    public abstract boolean getPackExists(String id);
}
