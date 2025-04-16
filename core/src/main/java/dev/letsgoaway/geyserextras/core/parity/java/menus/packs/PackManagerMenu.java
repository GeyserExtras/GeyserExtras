package dev.letsgoaway.geyserextras.core.parity.java.menus.packs;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.api.pack.ResourcePack;
import org.geysermc.geyser.api.pack.ResourcePackManifest;

import java.util.UUID;

public class PackManagerMenu extends BedrockMenu {
    private final ResourcePack pack;
    private final UUID packID;

    private boolean openingSubMenu = false;

    public PackManagerMenu(ResourcePack pack) {
        super();
        this.pack = pack;
        this.packID = pack.manifest().header().uuid();
    }


    @Override
    public SimpleForm create(ExtrasPlayer player) {
        ResourcePackManifest.Header header = pack.manifest().header();
        setTitle(header.name());
        String info = header.description() + "\n\n" + "%manifestvalidation.packid " + header.uuid() + "\n" +
                "%manifestvalidation.packversion " + header.version().toString() + "\n";
        setHeader(info);


        if (!player.getPreferences().getSelectedPacks().contains(packID)) {
            add(new Button(BedrockLocale.PACKS.SELECT, FormImage.Type.PATH, "textures/ui/plus.png", () -> {
                addPack(player);
            }));
        } else {
            int pos = player.getPreferences().getSelectedPacks().indexOf(packID);
            if (pos != 0) {
                add(new Button(BedrockLocale.PACKS.UP, FormImage.Type.PATH, "textures/ui/up_arrow.png", () -> {
                    up(player);
                }));
            }
            add(new Button(BedrockLocale.PACKS.REMOVE, FormImage.Type.PATH, "textures/ui/minus.png", () -> {
                removePack(player);
            }));
            if (pos != player.getPreferences().getSelectedPacks().size() - 1) {
                add(new Button(BedrockLocale.PACKS.DOWN, FormImage.Type.PATH, "textures/ui/down_arrow.png", () -> {
                    down(player);
                }));
            }
            /*
            if (!pack.manifest().subpacks().isEmpty()) {
                add(new Button(BedrockLocale.SETTINGS, FormImage.Type.PATH, "textures/ui/settings_glyph_color_2x.png", () -> {
                    player.sendForm(new PackSettingsMenu(pack));
                    openingSubMenu = true;
                }));
            }*/
        }
        return super.create(player);
    }

    @Override
    public void onClose(ExtrasPlayer player) {
        super.onClose(player);
        player.sendForm(new PackMenu());
    }

    @Override
    public void onButtonClick(ExtrasPlayer player) {
        super.onButtonClick(player);
        if (!openingSubMenu) {
            player.setPacksUpdated(true);
            player.sendForm(new PackMenu());
        }
    }

    private void addPack(ExtrasPlayer player) {
        player.getPreferences().getSelectedPacks().add(0, packID);
    }

    private void removePack(ExtrasPlayer player) {
        player.getPreferences().getSelectedPacks().remove(packID);
    }

    private void up(ExtrasPlayer player) {
        int lastPos = player.getPreferences().getSelectedPacks().indexOf(packID);
        player.getPreferences().getSelectedPacks().remove(packID);
        player.getPreferences().getSelectedPacks().add(Math.max(0, lastPos - 1), packID);
    }

    private void down(ExtrasPlayer player) {
        int lastPos = player.getPreferences().getSelectedPacks().indexOf(packID);
        player.getPreferences().getSelectedPacks().remove(packID);
        player.getPreferences().getSelectedPacks().add(lastPos + 1, packID);
    }
}
