package dev.letsgoaway.geyserextras.spigot.api;

import dev.letsgoaway.geyserextras.spigot.player.PlayerDevice;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;

import java.nio.file.Path;
import java.util.UUID;

 interface IBedrockPluginAPI {
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


     boolean getPackExists(String id);

     void onConfigLoad();

     void hidePaperDoll(UUID uuid);

     int getPing(UUID uuid);

     void swapOffhand(UUID uuid);
}
