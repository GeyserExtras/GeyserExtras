package dev.letsgoaway.geyserextras.core.protocol;


import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import org.geysermc.geyser.session.GeyserSession;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class CapeLoader {
    public enum Cape {
        ANNIVERSARY15TH(UUID.fromString("7ed341e7-6bd3-44e2-8899-7ab99fad34e8"), "15thanniversary"),
        CHERRY(UUID.fromString("0302ebba-bfeb-8748-3e66-03acd664c78f"), "cherry"),
        COMMON(UUID.fromString("3b1f4153-8cec-4407-b287-83bc5962fd0b"), "common"),
        COPPER(UUID.fromString("c092dcde-7bc1-ac79-5796-1106273398a6"), "copper"),
        EYEBLOSSOM(UUID.fromString("24372814-36c0-4ac9-8de7-7086f504fa97"), "eyeblossom"),
        FOLLOWERS(UUID.fromString("626c9afc-3d6b-4c90-826a-863996e76e9a"), "followers"),
        FOUNDERS(UUID.fromString("1b99b77c-3caa-4aaa-8f79-f79f053e586f"), "founders"),
        HOME(UUID.fromString("a4c83b58-b18e-0815-7ce7-1ec9182d8027"), "home"),
        MCC(UUID.fromString("d9ad40f5-e119-4b2b-8077-cec04b923e60"), "mcc15thyear"),
        MC_EXPERIENCE(UUID.fromString("efd2b594-49bb-48b9-b9f2-9b0ac2ef3af4"),"mcexperience"),
        MENACE(UUID.fromString("3f4f5068-2c40-debb-b181-bdafad187859"), "menace"),
        PANCAPE(UUID.fromString("ef479b6d-7072-47aa-8985-0f025cd24cdb"), "pan"),
        PURPLE_HEART(UUID.fromString("07adfe62-a54b-49b4-9d93-10c6c2079a76"), "purpleheart"),
        VANILLA(UUID.fromString("fce20ac3-e80b-4c00-8230-d815c8a0665c"),"vanilla"),
        YEARN(UUID.fromString("5b20560b-6da2-4c2f-ba1c-3d94fea845eb"), "yearn"),

        MINECON_2016(UUID.fromString("918a35c9-9af0-30d3-b7ca-39981e49284f"), "minecon2016"),
        MINECON_2015(UUID.fromString("57c944ac-5f69-369d-b54f-209aa8f3c670"), "minecon2015"),
        MINECON_2013(UUID.fromString("f88ed3ec-2e5d-35ca-b0fc-e710ff9d5e9a"), "minecon2013"),
        MINECON_2012(UUID.fromString("5a8f4a08-3a77-3823-9b26-48fa07ff8270"), "minecon2012"),
        MINECON_2011(UUID.fromString("b52f7287-df73-3114-b0eb-e883e2cbd43e"), "minecon2011");

        final UUID bedrockId;
        final String definitionId;

        Cape(UUID bedrockId, String definitionId) {
            this.bedrockId = bedrockId;
            this.definitionId = definitionId;
        }
    }

    public static Map<UUID, JsonObject> UUID_TO_JAVA_PROFILE = new HashMap<>();

    public static void init() {
        for (Cape cape : Cape.values()) {
            InputStream stream = Cape.class.getClassLoader().getResourceAsStream("capes/" + cape.definitionId + ".json");
            if (stream != null) {
                try {
                    String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    UUID_TO_JAVA_PROFILE.put(cape.bedrockId, JsonParser.parseString(json).getAsJsonObject());
                } catch (IOException e) {
                    SERVER.warn("Could not load cape: " + cape.name());
                }
            } else {
                SERVER.warn("Could not load cape: " + cape.name());

            }
        }
    }

    public static ItemProfile getAsItemProfile(UUID capeUUID) {
        JsonObject object = UUID_TO_JAVA_PROFILE.get(capeUUID);

        return new ItemProfile(
                object.get("name").getAsString(),
                UUID.fromString(addDashes(object.get("id").getAsString())),
                List.of(new ItemProfile.Property(
                        "textures",
                        object.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString(),
                        object.getAsJsonArray("properties").get(0).getAsJsonObject().get("signature").getAsString()
                )),

                new ItemProfile.SkinPatch(ResourceLocation.minecraft("gui/tab_header_background"), null, null, null));
    }

    //https://stackoverflow.com/a/32592722
    public static String addDashes(String idNoDashes) {
        StringBuffer idBuff = new StringBuffer(idNoDashes);
        idBuff.insert(20, '-');
        idBuff.insert(16, '-');
        idBuff.insert(12, '-');
        idBuff.insert(8, '-');
        return idBuff.toString();
    }

    public static UUID getPlayerCapeUUID(GeyserSession session) {
        UUID capeUUID;

        // Data UUID's are used for Minecon capes because
        // technically they are skin pack capes
        // and they dont have a uuid
        UUID dataUUID = UUID.nameUUIDFromBytes(session.getClientData().getCapeData());

        try {
            capeUUID = UUID.fromString(session.getClientData().getCapeId());
        } catch (IllegalArgumentException e) {
            return dataUUID;
        }

        // Bedrock prefers skin pack capes over equipped ones
        if (CapeLoader.exists(dataUUID)) {
            return dataUUID;
        }

        return capeUUID;
    }


    public static boolean exists(UUID uuid) {
        return UUID_TO_JAVA_PROFILE.containsKey(uuid);
    }

}
