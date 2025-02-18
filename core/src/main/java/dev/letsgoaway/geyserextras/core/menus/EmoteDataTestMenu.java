package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import org.cloudburstmc.protocol.bedrock.data.StoreOfferRedirectType;
import org.cloudburstmc.protocol.bedrock.packet.ShowStoreOfferPacket;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;

import java.util.UUID;

public class EmoteDataTestMenu extends BedrockMenu {

    @Override
    public SimpleForm create(ExtrasPlayer player) {
        // if this works this might be the most cooked shit ever made
        EmoteUtils.emotes.keySet().forEach((id) -> {
            UUID emote = UUID.fromString(id);

            String thumbnail = EmoteUtils.getThumbnail(emote);
            if (thumbnail != null) {
                add(new Button(EmoteUtils.getName(emote), FormImage.Type.URL, thumbnail, () -> {
                    player.sendForm(new EmoteDataMenu(emote));
                }));
            } else {
                add(new Button(EmoteUtils.getName(emote), () -> {
                    player.sendForm(new EmoteDataMenu(emote));
                }));
            }
        });

        return super.create(player);
    }

    public static class EmoteDataMenu extends BedrockMenu {
        private UUID emote;

        public EmoteDataMenu(UUID emote) {
            super();
            this.emote = emote;
        }

        @Override
        public SimpleForm create(ExtrasPlayer player) {
            setTitle("Emote Data");
            setHeader("Emote: " + EmoteUtils.getName(emote) + "\n"
                    + "Emote ID: " + emote.toString() + "\n"
                    + "Emote Thumbnail: " + EmoteUtils.getThumbnail(emote) + "\n"
                    + "Emote Creator: " + EmoteUtils.getCreator(emote) + "\n"
                    + "Emote Rarity: " + EmoteUtils.getRarity(emote) + "\n"
                    + "Emote Price: " + EmoteUtils.getPrice(emote) + "\n"
            );
            String thumbnail = EmoteUtils.getThumbnail(emote);
            add(new Button("Show Store Page", () -> {
                ShowStoreOfferPacket packet = new ShowStoreOfferPacket();
                packet.setOfferId(emote.toString());
                packet.setRedirectType(StoreOfferRedirectType.DRESSING_ROOM);
                player.getSession().sendUpstreamPacket(packet);
            }));

            add(new Button("Play Animation", () -> {
                player.getSession().getEntityCache().getAllPlayerEntities().forEach((entity) -> {
                    player.getSession().showEmote(entity, emote.toString());
                });
            }));
            if (thumbnail != null) {
                add(new Button("Back", FormImage.Type.URL, thumbnail, () -> {
                    player.sendForm(new EmoteDataTestMenu());
                }));
            } else {
                add(new Button("Back", () -> {
                    player.sendForm(new EmoteDataTestMenu());
                }));
            }
            return super.create(player);
        }

    }
}
