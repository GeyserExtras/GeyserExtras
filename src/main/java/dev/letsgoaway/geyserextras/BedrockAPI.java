package dev.letsgoaway.geyserextras;

import dev.letsgoaway.geyserextras.api.BedrockPluginAPI;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.cumulus.form.Form;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class BedrockAPI {
    private Class<?> apiClass = null;
    private BedrockPluginAPI apiInstance = null;

    public HashMap<APIType, BedrockPluginAPI> apiInstances = new HashMap<>();
    public APIType apiType = null;
    public boolean foundGeyserClasses = false;

    private boolean classExists(String path) {
        boolean exists = false;
        try {
            apiClass = Class.forName(path);
            exists = true;
        } catch (ClassNotFoundException ignored) {
        }
        return exists;
    }

    public BedrockAPI() {
        if (classExists("org.geysermc.floodgate.api.FloodgateApi")) {
            apiType = APIType.FLOODGATE;
            apiInstances.put(APIType.FLOODGATE, new dev.letsgoaway.geyserextras.api.FloodgateBedrockAPI());
            foundGeyserClasses = true;
        }
        if (classExists("org.geysermc.api.GeyserApiBase")) {
            apiType = APIType.GEYSER;
            apiInstances.put(APIType.GEYSER, new dev.letsgoaway.geyserextras.api.GeyserBedrockAPI());
            foundGeyserClasses = true;
        }
        if (apiInstances.containsKey(APIType.FLOODGATE)) {
            apiInstance = apiInstances.get(APIType.FLOODGATE);
        } else if (apiInstances.containsKey(APIType.GEYSER)) {
            apiInstance = apiInstances.get(APIType.GEYSER);
        }
    }

    public boolean isBedrockPlayer(@NonNull UUID uuid) {
        return apiInstance.isBedrockPlayer(uuid);
    }

    public boolean sendForm(@NonNull UUID uuid, @NonNull Form form) {
        return apiInstance.sendForm(uuid, form);
    }

    public void reconnect(@NonNull UUID uuid) {
        apiInstance.reconnect(uuid);
    }

    public PlayerDevice getPlayerDevice(BedrockPlayer bedrockPlayer) {
        return apiInstance.getPlayerDevice(bedrockPlayer.player.getUniqueId());
    }

    public PlayerInputType getPlayerInputType(BedrockPlayer bedrockPlayer) {
        return apiInstance.getPlayerInputType(bedrockPlayer.player.getUniqueId());
    }

    public PlayerUIProfile getPlayerUIProfile(BedrockPlayer bedrockPlayer) {
        return apiInstance.getPlayerUIProfile(bedrockPlayer.player.getUniqueId());
    }

    public String getXboxUsername(BedrockPlayer bedrockPlayer) {
        return apiInstance.getXboxUsername(bedrockPlayer.player.getUniqueId());
    }

    public String getPlayerXUID(BedrockPlayer bedrockPlayer) {
        return apiInstance.getPlayerXUID(bedrockPlayer.player.getUniqueId());
    }

    public boolean isLinked(BedrockPlayer bedrockPlayer) {
        return apiInstance.isLinked(bedrockPlayer.player.getUniqueId());
    }

    public void sendFog(BedrockPlayer bedrockPlayer, String fogID) {
        if (Config.proxyMode) {
            String fog = "s" + fogID;
            bedrockPlayer.player.sendPluginMessage(GeyserExtras.plugin, "geyserextras:fog", ("s" + fogID).getBytes(StandardCharsets.UTF_8));
        } else if (apiInstances.containsKey(APIType.GEYSER)) {
            apiInstances.get(APIType.GEYSER).sendFog(bedrockPlayer.player.getUniqueId(), fogID);
        }
    }

    public void removeFog(BedrockPlayer bedrockPlayer, String fogID) {
        if (Config.proxyMode) {
            String fog = "r" + fogID;
            bedrockPlayer.player.sendPluginMessage(GeyserExtras.plugin, "geyserextras:fog", fog.getBytes(StandardCharsets.UTF_8));
        } else if (apiInstances.containsKey(APIType.GEYSER)) {
            apiInstances.get(APIType.GEYSER).removeFog(bedrockPlayer.player.getUniqueId(), fogID);
        }
    }

    public void onLoadConfig(){
        for (BedrockPluginAPI api : apiInstances.values()){
            api.onConfigLoad();
        }
    }
}
