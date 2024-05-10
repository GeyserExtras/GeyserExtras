package dev.letsgoaway.geyserextras.spigot.api;

import dev.letsgoaway.geyserextras.spigot.player.PlayerDevice;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;

import java.nio.file.Path;
import java.util.UUID;

public interface IBedrockPluginAPI {
    public boolean isBedrockPlayer(UUID uuid);

    public boolean sendForm(UUID uuid, Form form);

    public boolean sendForm(UUID uuid, FormBuilder<?, ?, ?> form);

    public void reconnect(UUID uuid);

    public PlayerDevice getPlayerDevice(UUID uuid);

    public String getXboxUsername(UUID uuid);

    public String getPlayerXUID(UUID uuid);

    public boolean isLinked(UUID uuid);

    public void sendFog(UUID uniqueId, String fog);

    public void removeFog(UUID uuid, String fog);

    public UUID getPackID(Path path);

    public String getPackName(String id);

    public String getPackDescription(String id);

    public String getPackVersion(String id);


    public boolean getPackExists(String id);

    public void onConfigLoad();
}
