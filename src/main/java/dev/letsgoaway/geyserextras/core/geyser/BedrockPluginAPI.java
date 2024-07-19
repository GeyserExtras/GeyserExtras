package dev.letsgoaway.geyserextras.core.geyser;

import dev.letsgoaway.geyserextras.spigot.player.PlayerDevice;
import dev.letsgoaway.geyserextras.spigot.player.PlayerInputType;
import dev.letsgoaway.geyserextras.spigot.player.PlayerUIProfile;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.UUID;

public interface BedrockPluginAPI {
     void disable();
     boolean isBedrockPlayer(UUID uuid);

     boolean sendForm(UUID uuid, Form form);

     boolean sendForm(UUID uuid, FormBuilder<?, ?, ?> form);

     void reconnect(UUID uuid);

     PlayerDevice getPlayerDevice(UUID uuid);

     String getXboxUsername(UUID uuid);

     String getPlayerXUID(UUID uuid);

     boolean isLinked(UUID uuid);

     void sendFog(UUID uniqueId, String fog);

     void removeFog(UUID uuid, String fog);

     UUID getPackID(Path path);

     String getPackName(String id);

     String getPackDescription(String id);

     String getPackVersion(String id);


     @Nullable
     Path getPackPath(String id);

     boolean getPackExists(String id);

     void onConfigLoad();

     void hidePaperDoll(UUID uuid);

    PlayerInputType getPlayerInputType(UUID uniqueId);

    PlayerUIProfile getPlayerUIProfile(UUID uniqueId);
}
