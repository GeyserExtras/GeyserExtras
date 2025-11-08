package dev.letsgoaway.geyserextras.core.protocol.handlers.workarounds;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import dev.letsgoaway.geyserextras.core.protocol.CapeLoader;
import dev.letsgoaway.geyserextras.core.protocol.handlers.JavaPacketHandler;
import dev.letsgoaway.geyserextras.core.protocol.JavaPlayer;
import dev.letsgoaway.geyserextras.core.utils.IdUtils;
import org.geysermc.geyser.session.GeyserSession;

import java.util.*;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

// This is the Mannequin work around for Capes.
// Basically we spawn a fake mannequin entity, load a java player's profile
// that has a specific cape equipped (see resources/capes/README.txt)
// replace the players body texture using a invisible texture in the vanilla resource pack
// and make that mannequin copy the bedrock players position / velocity packets.
// We can't just use the mannequin as the bedrock player, because
// they can't process the EntityAnimation packet (things like swinging arms).

// Some issues with this approach:

/* MOUNTS */
// Mount's that can have more than one player mounted at a time do not work
// as translating the SetPassengers packet mounts the Mannequin as another player.
// Even Boat Chests, which can only have one player, render the mannequin inside the chest
// as they probably extend the boat class in Minecraft Java's source code and that would be
// where the second player's position is.

// The cape is offset on Java players when wearing armor, however
// this workaround cannot do that when inside a mount, so it renders incorrectly.
// We wont put armor on the mannequin because it would misalign with the players arm when they
// swing which arguably looks worse than it clipping a little bit at the top.

/* PLAYER BODY ROTATION */
// When a player swings their arm, their body rotates to match where they are looking.
// The mannequin can't recieve the arm swing animation, so the body does not sync up.
// This results in a visual bug where the cape can clip inside the players body
// when they swing their arm while standing still.
// When they are moving, their body does not rotate while
// swinging their arm so this does not occur.

/* RELIANCE ON PLAYERS */
// We have to load profiles which might change their cape.
// The requests from Mojang api are save under JSON's in the resource
// which likely means that when a profile used changes their cape
// it will be invalid and not render the capes on Java Clients,
// but I haven't tested it.

/* POSSIBILITY FOR HITBOX DESYNC */
// This mannequin approach also replaces attack packets directed
// towards the mannequin's id with the player's id
// as otherwise PVP would not work because the mannequin's hitbox
// intersects with the players.
// you have never seen jank like this before
// prepare yourself
public class MannequinCapeWorkaround implements JavaPacketHandler {
    // mounts that can support the cape workaround because mounting multiple entities at once renders them in the save place
    public static List<EntityType> SINGLE_PLAYER_MOUNTS =
            List.of(
                    EntityTypes.MINECART,
//                    EntityTypes.ACACIA_CHEST_BOAT,
//                    EntityTypes.BIRCH_CHEST_BOAT,
//                    EntityTypes.CHERRY_CHEST_BOAT,
//                    EntityTypes.OAK_CHEST_BOAT,
//                    EntityTypes.BAMBOO_CHEST_RAFT,
//                    EntityTypes.JUNGLE_CHEST_BOAT,
//                    EntityTypes.DARK_OAK_CHEST_BOAT,
//                    EntityTypes.MANGROVE_CHEST_BOAT,
//                    EntityTypes.PALE_OAK_CHEST_BOAT,
//                    EntityTypes.SPRUCE_CHEST_BOAT,
                    EntityTypes.PIG,
                    EntityTypes.HORSE,
                    EntityTypes.ABSTRACT_HORSE,
                    EntityTypes.CHESTED_HORSE,
                    EntityTypes.ZOMBIE_HORSE,
                    EntityTypes.SKELETON_HORSE,
                    EntityTypes.DONKEY,
                    EntityTypes.MULE,
                    EntityTypes.HOGLIN,
                    EntityTypes.STRIDER
            );

    Map<Integer, UUID> players;
    List<Integer> mannequins;
    List<Integer> singlePlayerMounts = new ArrayList<>();


    public MannequinCapeWorkaround() {
        players = new HashMap<>();
        mannequins = new ArrayList<>();
    }


    private void deleteMannequin(User user, int mannequinID) {
        WrapperPlayServerDestroyEntities wrapper = new WrapperPlayServerDestroyEntities(mannequinID);
        user.sendPacketSilently(wrapper);
        mannequins.remove(Integer.valueOf(mannequinID));
        // todo: make the mannequin checks seperate from players that should apply the workaround
        players.remove(Integer.valueOf(-mannequinID));
    }

    @Override
    public void onPacketSend(JavaPlayer player, PacketSendEvent event) {
        User user = event.getUser();

        if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_METADATA)) {
            WrapperPlayServerEntityMetadata meta = new WrapperPlayServerEntityMetadata(event);
            event.markForReEncode(false);
            if (players.containsKey(meta.getEntityId())) {

                List<EntityData<?>> metadata = new ArrayList<>(meta.getEntityMetadata());

                WrapperPlayServerEntityMetadata mannequin = new WrapperPlayServerEntityMetadata(
                        -meta.getEntityId(),
                        metadata
                );

                // weird fix to get it to align how it would send normally
                int indexOf16 = -1;
                int indexOf18 = -1;

                for (EntityData<?> data : metadata) {
                    if (data.getIndex() == 16) {
                        indexOf16 = metadata.indexOf(data);

                        continue;
                    }
                    if (data.getIndex() == 18) {
                        indexOf18 = metadata.indexOf(data);
                        continue;
                    }
                }
                if (indexOf18 != -1) {
                    // not sure why but this disconnects users, and its not even there all the time.
                    // seems to happen once you kill an enemy, maybe xp?
                    metadata.remove(indexOf18);
                }
                if (indexOf16 != -1) {
                    GeyserSession session = (GeyserSession) GE.geyserApi.connectionByUuid(players.get(meta.getEntityId()));
                    if (session != null) {
                        UUID capeUUID = CapeLoader.getPlayerCapeUUID(session);
                        SERVER.log(capeUUID.toString());

                        // if we get to this point we know that the cape
                        // is valid and can be loaded

                        metadata.add(indexOf16 + 1,
                                new EntityData<>(
                                        17,
                                        EntityDataTypes.RESOLVABLE_PROFILE,
                                        CapeLoader.getAsItemProfile(
                                                capeUUID
                                        )
                                )
                        );
                    }

                }
                user.sendPacketSilently(mannequin);

            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.SPAWN_ENTITY)) {
            WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(event);


            if (packet.getEntityType().equals(EntityTypes.PLAYER)) {
                UUID playerUUID = null;
                if (packet.getUUID().isPresent()) {
                    playerUUID = packet.getUUID().get();
                }
                // only apply the mannequin work around for bedrock players on floodgate, java players will load their cape fine
                if (playerUUID != null && GE.geyserApi.isBedrockPlayer(playerUUID) && IdUtils.isFloodgateID(playerUUID)) {
                    GeyserSession session = (GeyserSession) GE.geyserApi.connectionByUuid(playerUUID);

                    // if we can actually load their cape on java, lets do the workaround for this player
                    if (session != null && CapeLoader.exists(CapeLoader.getPlayerCapeUUID(session))) {
                        players.put(packet.getEntityId(), playerUUID);

                        WrapperPlayServerSpawnEntity mannequin = new WrapperPlayServerSpawnEntity(
                                -packet.getEntityId(),
                                Optional.of(UUID.randomUUID()),
                                EntityTypes.MANNEQUIN,
                                packet.getPosition(),
                                packet.getPitch(),
                                packet.getYaw(),
                                packet.getHeadYaw(),
                                packet.getData(),
                                packet.getVelocity()
                        );
                        mannequins.add(mannequin.getEntityId());
                        user.sendPacket(mannequin);
                    }
                }

            } else if (SINGLE_PLAYER_MOUNTS.contains(packet.getEntityType())) {
                singlePlayerMounts.add(packet.getEntityId());
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.DESTROY_ENTITIES)) {
            WrapperPlayServerDestroyEntities wrapper = new WrapperPlayServerDestroyEntities(event);

            ArrayList<Integer> ents = new ArrayList<>();
            for (int entityID : wrapper.getEntityIds()) {
                ents.add(entityID);
                if (players.containsKey(entityID)) {
                    ents.add(-entityID);
                    // intellij says unneccasary boxing but its required
                    // because otherwise it would remove an object
                    // at the index entityID which would cause
                    // an IndexOutOfBoundsException
                    players.remove(Integer.valueOf(entityID));
                    mannequins.remove(Integer.valueOf(-entityID));
                } else if (singlePlayerMounts.contains(entityID)) {
                    singlePlayerMounts.remove(Integer.valueOf(entityID));
                }
            }

            wrapper.setEntityIds(ents.stream().mapToInt(Integer::intValue).toArray());
            event.markForReEncode(true);
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_EQUIPMENT)) {
            WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(event);
            if (players.containsKey(wrapper.getEntityId())) {

                for (Equipment equipment : wrapper.getEquipment()) {
                    if (equipment.getSlot().equals(EquipmentSlot.CHEST_PLATE)) {
                        // Replace the real player entity elytra with air because the cape mannequin will wear it
                        if (equipment.getItem().is(ItemTypes.ELYTRA)) {
                            ItemStack.Builder air = new ItemStack.Builder();
                            equipment.setItem(air.type(ItemTypes.AIR).amount(0).build());
                            event.markForReEncode(true);

                            ItemStack.Builder elytra = new ItemStack.Builder();
                            WrapperPlayServerEntityEquipment mannequin = new WrapperPlayServerEntityEquipment(
                                    -wrapper.getEntityId(),
                                    List.of(new Equipment(EquipmentSlot.CHEST_PLATE, elytra.type(ItemTypes.ELYTRA).amount(1).build()))

                            );
                            user.sendPacketSilently(mannequin);
                        }
                        // When the player takes off their elytra / puts on something else, remove it on the mannequin
                        else {
                            ItemStack.Builder air = new ItemStack.Builder();

                            WrapperPlayServerEntityEquipment mannequin = new WrapperPlayServerEntityEquipment(
                                    -wrapper.getEntityId(),
                                    List.of(new Equipment(EquipmentSlot.CHEST_PLATE, air.type(ItemTypes.AIR).amount(0).build()))
                            );
                            user.sendPacketSilently(mannequin);
                            event.markForReEncode(false);
                        }
                        break;
                    }

                }
            }
        }
// TODO: figure out fix for when a player swings their arm and their body rotates but the mannequin doesnt

        //        else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_ANIMATION)) {
//            WrapperPlayServerEntityAnimation wrapper = new WrapperPlayServerEntityAnimation(event);
//            if (players.containsKey(wrapper.getEntityId())) {
//                GeyserSession player = (GeyserSession) GE.geyserApi.connectionByUuid(players.get(wrapper.getEntityId()));
//
//                if (player != null) {
//                    SERVER.log("player not null");
//                    SessionPlayerEntity entity = player.getPlayerEntity();
//                    if (entity != null) {
//                        WrapperPlayServerEntityHeadLook mannequin = new WrapperPlayServerEntityHeadLook(
//                                -wrapper.getEntityId(),
//                                entity.getHeadYaw() + 25
//                        );
//                        user.sendPacketSilently(mannequin);
//                    }
//                }
//            }
//        }
        else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_MOVEMENT)) {
            WrapperPlayServerEntityPositionSync wrapper = new WrapperPlayServerEntityPositionSync(event);
            if (players.containsKey(wrapper.getId())) {
                WrapperPlayServerEntityPositionSync mannequin = new WrapperPlayServerEntityPositionSync(
                        -wrapper.getId(),
                        new EntityPositionData(wrapper.getValues().getPosition(), wrapper.getValues().getDeltaMovement(), wrapper.getValues().getYaw(), wrapper.getValues().getPitch()),
                        wrapper.isOnGround()
                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.UPDATE_ATTRIBUTES)) {
            WrapperPlayServerUpdateAttributes wrapper = new WrapperPlayServerUpdateAttributes(event);
            if (players.containsKey(wrapper.getEntityId())) {
                WrapperPlayServerUpdateAttributes mannequin = new WrapperPlayServerUpdateAttributes(-wrapper.getEntityId(), wrapper.getProperties());
                user.sendPacketSilently(mannequin);

            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_POSITION_SYNC)) {
            WrapperPlayServerEntityPositionSync wrapper = new WrapperPlayServerEntityPositionSync(event);
            if (players.containsKey(wrapper.getId())) {
                WrapperPlayServerEntityPositionSync mannequin = new WrapperPlayServerEntityPositionSync(
                        -wrapper.getId(),
                        new EntityPositionData(wrapper.getValues().getPosition(), wrapper.getValues().getDeltaMovement(), wrapper.getValues().getYaw(), wrapper.getValues().getPitch()),
                        wrapper.isOnGround()
                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_RELATIVE_MOVE)) {
            WrapperPlayServerEntityRelativeMove wrapper = new WrapperPlayServerEntityRelativeMove(event);
            if (players.containsKey(wrapper.getEntityId())) {
                WrapperPlayServerEntityRelativeMove mannequin = new WrapperPlayServerEntityRelativeMove(
                        -wrapper.getEntityId(),
                        wrapper.getDeltaX(),
                        wrapper.getDeltaY(),
                        wrapper.getDeltaZ(),
                        wrapper.isOnGround()
                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION)) {
            WrapperPlayServerEntityRelativeMoveAndRotation wrapper = new WrapperPlayServerEntityRelativeMoveAndRotation(event);
            if (players.containsKey(wrapper.getEntityId())) {
                WrapperPlayServerEntityRelativeMoveAndRotation mannequin = new WrapperPlayServerEntityRelativeMoveAndRotation(
                        -wrapper.getEntityId(),
                        wrapper.getDeltaX(),
                        wrapper.getDeltaY(),
                        wrapper.getDeltaZ(),
                        wrapper.getYaw(),
                        wrapper.getPitch(),
                        wrapper.isOnGround()
                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_HEAD_LOOK)) {
            WrapperPlayServerEntityHeadLook wrapper = new WrapperPlayServerEntityHeadLook(event);
            if (players.containsKey(wrapper.getEntityId())) {
                WrapperPlayServerEntityHeadLook mannequin = new WrapperPlayServerEntityHeadLook(
                        -wrapper.getEntityId(),
                        wrapper.getHeadYaw()
                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_ROTATION)) {
            WrapperPlayServerEntityRotation wrapper = new WrapperPlayServerEntityRotation(event);
            if (players.containsKey(wrapper.getEntityId())) {

                WrapperPlayServerEntityRotation mannequin = new WrapperPlayServerEntityRotation(
                        -wrapper.getEntityId(),
                        wrapper.getYaw(),
                        wrapper.getPitch(),
                        wrapper.isOnGround()

                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_VELOCITY)) {
            WrapperPlayServerEntityVelocity wrapper = new WrapperPlayServerEntityVelocity(event);
            if (players.containsKey(wrapper.getEntityId())) {
                WrapperPlayServerEntityVelocity mannequin = new WrapperPlayServerEntityVelocity(
                        -wrapper.getEntityId(),
                        wrapper.getVelocity()
                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_TELEPORT)) {
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(event);
            if (players.containsKey(wrapper.getEntityId())) {
                WrapperPlayServerEntityTeleport mannequin = new WrapperPlayServerEntityTeleport(
                        -wrapper.getEntityId(),
                        wrapper.getValues(),
                        wrapper.getRelativeFlags(),
                        wrapper.isOnGround()
                );
                user.sendPacketSilently(mannequin);
            }
        } else if (event.getPacketType().equals(PacketType.Play.Server.SET_PASSENGERS)) {
            WrapperPlayServerSetPassengers wrapper = new WrapperPlayServerSetPassengers(event);

            if (singlePlayerMounts.contains(wrapper.getEntityId())) {
                ArrayList<Integer> ents = new ArrayList<>();
                for (int entityID : wrapper.getPassengers()) {
                    ents.add(entityID);
                    if (players.containsKey(entityID)) {
                        ents.add(-entityID);
                    }
                }

                wrapper.setPassengers(ents.stream().mapToInt(Integer::intValue).toArray());
                event.markForReEncode(true);
            } else {
                for (int entityID : wrapper.getPassengers()) {
                    if (players.containsKey(entityID)) {
                        deleteMannequin(user, -entityID);
                    }
                }

            }
//        } else if (event.getPacketType().equals(PacketType.Play.Server.PLAYER_ABILITIES)) {
//            WrapperPlayServerPlayerAbilities wrapper = new WrapperPlayServerPlayerAbilities(event);
//            wrapper.getMode()
        }
    }

    @Override
    public void onPacketReceive(JavaPlayer player, PacketReceiveEvent event) {
        if (event.getPacketType().equals(PacketType.Play.Client.INTERACT_ENTITY)) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            // convert a mannequin id to player id
            // if it is a player id this is fine
            int playerID = Math.abs(wrapper.getEntityId());
            if (players.containsKey(playerID)) {
                wrapper.setEntityId(playerID);
            }
        }
    }

    @Override
    public boolean runIfCancelled() {
        return true;
    }
}
