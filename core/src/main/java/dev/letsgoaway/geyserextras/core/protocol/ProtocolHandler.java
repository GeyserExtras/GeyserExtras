package dev.letsgoaway.geyserextras.core.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
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
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;

import java.util.*;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

// This contains work arounds for Java only clients
public class ProtocolHandler implements PacketListener {
    // you have never seen jank like this before
    // prepare yourself

    public static void load(PacketEventsAPI<?> builder) {
        PacketEvents.setAPI(builder);
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(new ProtocolHandler(), PacketListenerPriority.HIGHEST);
    }

    public static void init() {
        PacketEvents.getAPI().init();
        CapeLoader.init();
    }

    public static void terminate() {
        PacketEvents.getAPI().terminate();
    }


    Map<Integer, UUID> players = new HashMap<>();
    List<Integer> mannequins = new ArrayList<>();


    // mounts that can support the cape workaround because mounting multiple entities at once renders them in the save place
    public static List<EntityType> SINGLE_PLAYER_MOUNTS =
            List.of(
                    EntityTypes.MINECART,
                    EntityTypes.ACACIA_CHEST_BOAT,
                    EntityTypes.BIRCH_CHEST_BOAT,
                    EntityTypes.CHERRY_CHEST_BOAT,
                    EntityTypes.OAK_CHEST_BOAT,
                    EntityTypes.BAMBOO_CHEST_RAFT,
                    EntityTypes.JUNGLE_CHEST_BOAT,
                    EntityTypes.DARK_OAK_CHEST_BOAT,
                    EntityTypes.MANGROVE_CHEST_BOAT,
                    EntityTypes.PALE_OAK_CHEST_BOAT,
                    EntityTypes.SPRUCE_CHEST_BOAT,
                    EntityTypes.PIG

            );
    List<Integer> singlePlayerMounts = new ArrayList<>();

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        // Only handle this when we are sending packets to java players
        // Bedrock players get their packets in core/injectors/
        if (GeyserHandler.getPlayer(user.getUUID()) != null) {
            return;
        }
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

                for (EntityData<?> data : metadata) {
                    if (data.getIndex() == 16) {
                        indexOf16 = metadata.indexOf(data);

                        break;
                    }
                }
                if (indexOf16 != -1) {
                    GeyserSession player = (GeyserSession) GE.geyserApi.connectionByUuid(players.get(meta.getEntityId()));
                    if (player != null) {
                        UUID capeUUID = CapeLoader.getPlayerCapeUUID(player);
                        SERVER.log(capeUUID.toString());
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
                // only apply the mannequin work around for bedrock players, java players will load their cape fine
                if (playerUUID != null && GE.geyserApi.isBedrockPlayer(playerUUID)) {
                    GeyserSession player = (GeyserSession) GE.geyserApi.connectionByUuid(playerUUID);

                    // if we can actually load their cape on java lets do that
                    if (player != null && CapeLoader.exists(CapeLoader.getPlayerCapeUUID(player))) {
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
                        deleteMannequin(user, entityID);
                    }
                }

            }
        }
    }

    private void deleteMannequin(User user, int mannequinID) {
        WrapperPlayServerDestroyEntities wrapper = new WrapperPlayServerDestroyEntities(mannequinID);
        user.sendPacketSilently(wrapper);
        mannequins.remove(Integer.valueOf(mannequinID));

    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
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
}
