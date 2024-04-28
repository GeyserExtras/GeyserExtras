package dev.letsgoaway.geyserextras.menus;

import dev.letsgoaway.geyserextras.BedrockPlayer;
import dev.letsgoaway.geyserextras.EmoteUtils;
import dev.letsgoaway.geyserextras.form.BedrockContextMenu;
import dev.letsgoaway.geyserextras.form.elements.Button;
import org.geysermc.cumulus.util.FormImage;

import java.util.Arrays;

public class QuickMenuSetup extends BedrockContextMenu {
    public QuickMenuSetup(BedrockPlayer bplayer) {
        super("Quick-Menu Bindings");
        StringBuilder headerText = new StringBuilder();
        for (String s : bplayer.quickMenuList) {
            headerText.append("Emote #" + String.valueOf(bplayer.quickMenuList.indexOf(s) + 1) + " ID: " + EmoteUtils.getEmoteName(s) + "\n");
        }

        simpleForm.content(headerText.toString());
        add(new Button("Set Emote #1", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_0.png", () -> {
            bplayer.setWaiting(0);
        }));

        add(new Button("Set Emote #2", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_1.png", () -> {
            bplayer.setWaiting(1);
        }));

        add(new Button("Set Emote #3", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_2.png", () -> {
            bplayer.setWaiting(2);
        }));

        add(new Button("Set Emote #4", FormImage.Type.PATH, "textures/ui/emote_wheel_updated_select_3.png", () -> {
            bplayer.setWaiting(3);
        }));
        add(new Button("Clear All", FormImage.Type.PATH, "textures/ui/icon_trash.png", () -> {
            bplayer.quickMenuList = Arrays.asList("", "", "", "");
            new QuickMenuSetup(bplayer).show(bplayer);
        }));
    }
}
