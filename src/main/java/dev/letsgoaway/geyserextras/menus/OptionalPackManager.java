package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.APIType;
import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.Config;
import dev.letsgoaway.geyserextras.GeyserExtras;
import dev.letsgoaway.geyserextras.api.BedrockPluginAPI;
import dev.letsgoaway.geyserextras.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.form.elements.Button;
import org.checkerframework.checker.units.qual.A;
import org.geysermc.cumulus.util.FormImage;

import java.io.File;
import java.util.UUID;

public class OptionalPackManager extends BedrockContextMenu {
    public static String getDescriptionText(String packid) {
        BedrockPluginAPI geyserApi = GeyserExtras.bedrockAPI.apiInstances.get(APIType.GEYSER);
        return geyserApi.getPackDescription(packid) + "\n\n" +
                "Pack ID: " + geyserApi.getPackID(geyserApi.getPackPath(packid)) +
                "\nPack Version: "+ geyserApi.getPackVersion(packid);
    }
    public OptionalPackManager(BedrockPlayer bplayer, String packID, String name) {
        super(name,  getDescriptionText(packID));
        BedrockPluginAPI geyserApi = GeyserExtras.bedrockAPI.apiInstances.get(APIType.GEYSER);
        this.onClose = ()->{
            new OptionalPacks(bplayer).show(bplayer);
        };
        if (!bplayer.optionalPacks.contains(packID)){
            add(new Button("Add", FormImage.Type.PATH, "textures/ui/plus.png",()->{
               bplayer.addPack(geyserApi.getPackPath(packID));
                new OptionalPacks(bplayer).show(bplayer);
            }));
        }
        else {
            if (bplayer.optionalPacks.indexOf(packID) != 0){
                add(new Button("Move Up", FormImage.Type.PATH, "textures/ui/up_arrow.png",()->{
                    bplayer.movePackUp(geyserApi.getPackPath(packID));
                    new OptionalPacks(bplayer).show(bplayer);
                }));
            }
            add(new Button("Remove", FormImage.Type.PATH, "textures/ui/minus.png",()->{
                bplayer.removePack(geyserApi.getPackPath(packID));
                new OptionalPacks(bplayer).show(bplayer);
            }));
            if (bplayer.optionalPacks.indexOf(packID) != bplayer.optionalPacks.size()-1){
                add(new Button("Move Down", FormImage.Type.PATH, "textures/ui/down_arrow.png",()->{
                    bplayer.movePackDown(geyserApi.getPackPath(packID));
                    new OptionalPacks(bplayer).show(bplayer);
                }));
            }

        }
    }
}
