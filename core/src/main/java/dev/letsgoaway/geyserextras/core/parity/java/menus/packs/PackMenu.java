package dev.letsgoaway.geyserextras.core.parity.java.menus.packs;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.pack.ResourcePack;
import org.geysermc.geyser.text.ChatColor;

import java.util.List;
import java.util.UUID;

public class PackMenu extends BedrockMenu {
    private boolean openingSubMenu = false;

    @Override
    public SimpleForm create(ExtrasPlayer player) {
        setTitle(BedrockLocale.MENU.RESOURCE_PACKS);

        add(new Button(ChatColor.BOLD + player.translate("pack.selected.title"), () -> {
            player.sendForm(new PackMenu());
        }));

        List<UUID> selectedPacks = player.getPreferences().getSelectedPacks();

        for (UUID uuid : selectedPacks) {
            createPackButton(PackLoader.PACKS.get(uuid), player);
        }

        add(new Button(ChatColor.BOLD + player.translate("pack.available.title"), () -> {
            player.sendForm(new PackMenu());
        }));

        PackLoader.PACKS.forEach((uuid, pack)->{
            if (!selectedPacks.contains(uuid)){
                createPackButton(pack, player);
            }
        });

        return super.create(player);
    }

    @Override
    public void onClose(ExtrasPlayer player) {
        super.onClose(player);
        if (!openingSubMenu && player.isPacksUpdated()) {
            player.getPreferences().save();
            player.reconnect();
        }
    }

    private void createPackButton(ResourcePack pack, ExtrasPlayer player) {
        this.add(new Button(pack.manifest().header().name(), () -> {
            openingSubMenu = true;
            player.sendForm(new PackManagerMenu(pack));
        }));
    }
}
