package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.BedrockMenu;
import dev.letsgoaway.geyserextras.core.form.BedrockModal;
import dev.letsgoaway.geyserextras.core.form.elements.Button;
import dev.letsgoaway.geyserextras.core.form.elements.TextInput;
import dev.letsgoaway.geyserextras.core.parity.bedrock.EmoteUtils;
import org.cloudburstmc.protocol.bedrock.data.StoreOfferRedirectType;
import org.cloudburstmc.protocol.bedrock.packet.ShowStoreOfferPacket;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Pose;

import java.util.Arrays;
import java.util.UUID;

public class EmoteDataTestMenu extends BedrockMenu {
    @Override
    public SimpleForm create(ExtrasPlayer player) {
        // ok so basically we can rip every emotes data using this shit yipee
        // when it sends an animation uuid it downloads / requests all the data we need
        // we can then capture it with fiddler classic for the EmoteExtractor
        // For this to work another player has to be online on the server
        add(new Button("Search", () -> {
            player.sendForm(new EmoteSearchMenu());
        }));


        add(new Button("Download All", () -> {
            new Thread(() -> {
                int totalDownloaded = 0;
                int totalAmount = EmoteUtils.emotes.keySet().size();
                // do this slowly without spamming so microsoft doesnt rate limit the SHIT out of me lmao
                long downloadRate = 7500L;

                for (String id : EmoteUtils.emotes.keySet()) {

                    UUID uuid = UUID.fromString(id);

                    long totalSecs = (totalAmount - totalDownloaded) * ((downloadRate) / 1000L);
                    long hours = totalSecs / 3600;
                    long minutes = (totalSecs % 3600) / 60;
                    long seconds = totalSecs % 60;

                    String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                    totalDownloaded++;

                    player.sendToast("Downloading emote #" + totalDownloaded, "Estimated time: " + timeString + " | " + totalDownloaded + " / " + totalAmount);
                    player.sendMessage("UUID: " + uuid);
                    player.sendMessage("Name: " + EmoteUtils.getName(uuid));
                    player.sendMessage("Creator: " + EmoteUtils.getCreator(uuid));
                    player.sendMessage("Price: " + EmoteUtils.getPrice(uuid) + "\uE102");
                    player.sendMessage("Message: " + EmoteUtils.getEmoteChatString(uuid, player, EmoteUtils.EmoteTextType.MESSAGE));
                    player.sendMessage("Special Message: " + EmoteUtils.getEmoteChatString(uuid, player, EmoteUtils.EmoteTextType.SPECIAL_MESSAGE));


                    player.getSession().getEntityCache().getAllPlayerEntities().forEach((entity) -> {
                        entity.setPose(Pose.SNEAKING);
                        entity.updateBedrockMetadata();
                        entity.setPose(Pose.STANDING);
                        entity.updateBedrockMetadata();
                        player.getSession().showEmote(entity, id);
                    });

                    try {
                        Thread.sleep(downloadRate);
                    } catch (InterruptedException ignored) {
                    }
                }
                player.sendToast("Download complete!", "GG");
            }).start();
        }));


        add(new Button("Load Emote List", () -> {
            player.sendForm(new EmoteListMenu());
        }));

        return super.create(player);
    }

    public static class EmoteSearchMenu extends BedrockForm {
        @Override
        public CustomForm.Builder create(ExtrasPlayer player) {
            add(new TextInput("Emote UUID", (id) -> {
                UUID emote = UUID.fromString(id);
                if (!EmoteUtils.emotes.has(id)) {
                    player.sendForm(new EmoteNotFoundModal(id));
                } else {
                    player.sendForm(new EmoteDataMenu(emote, true));
                }
            }));
            return super.create(player);
        }


        public static class EmoteNotFoundModal extends BedrockModal {
            private final String id;

            public EmoteNotFoundModal(String id) {
                super();
                this.id = id;
            }

            @Override
            public ModalForm create(ExtrasPlayer player) {
                setTitle("Emote not found!");
                setContent("Emote with id " + id + " was not found in database!");
                setYesText("Download and Return to Search");
                setNoText("Return to Search");
                return super.create(player);
            }

            @Override
            public void onSubmit(ExtrasPlayer player, boolean accepted) {
                if (accepted) {
                    player.getSession().getEntityCache().getAllPlayerEntities().forEach((entity) -> {
                        entity.setPose(Pose.SNEAKING);
                        entity.updateBedrockMetadata();
                        entity.setPose(Pose.STANDING);
                        entity.updateBedrockMetadata();
                        player.getSession().showEmote(entity, id);
                    });
                }
                player.sendForm(new EmoteSearchMenu());
                super.onSubmit(player, accepted);
            }
        }
    }

    public static class EmoteListMenu extends BedrockMenu {
        public EmoteListMenu() {
            super();
        }

        @Override
        public SimpleForm create(ExtrasPlayer player) {
            EmoteUtils.emotes.keySet().forEach((id) -> {
                UUID emote = UUID.fromString(id);

                String thumbnail = EmoteUtils.getThumbnail(emote);
                if (thumbnail != null) {
                    add(new Button(EmoteUtils.getName(emote), FormImage.Type.URL, thumbnail, () -> {
                        player.sendForm(new EmoteDataMenu(emote, false));
                    }));
                } else {
                    add(new Button(EmoteUtils.getName(emote), () -> {
                        player.sendForm(new EmoteDataMenu(emote, false));
                    }));
                }
            });
            return super.create(player);
        }
    }

    public static class EmoteDataMenu extends BedrockMenu {
        private final boolean returnToSearch;
        private final UUID emote;

        public EmoteDataMenu(UUID emote, boolean returnToSearch) {
            super();
            this.emote = emote;
            this.returnToSearch = returnToSearch;
        }

        @Override
        public void onClose(ExtrasPlayer player) {
            if (returnToSearch) {
                player.sendForm(new EmoteSearchMenu());
            } else {
                player.sendForm(new EmoteDataTestMenu());
            }
        }

        @Override
        public SimpleForm create(ExtrasPlayer player) {
            setTitle("Emote Data");
            setHeader("Emote: " + EmoteUtils.getName(emote) + "\n" + "Emote ID: " + emote + "\n" + "Emote Thumbnail: " + EmoteUtils.getThumbnail(emote) + "\n" + "Emote Creator: " + EmoteUtils.getCreator(emote) + "\n" + "Emote Rarity: " + EmoteUtils.getRarity(emote) + "\n" + "Emote Price: " + EmoteUtils.getPrice(emote) + "\uE102\n");
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