package dev.letsgoaway.geyserextras.core.parity.java.blockdisplay;

import dev.letsgoaway.geyserextras.core.utils.MathUtils;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AnimateEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket;
import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.entity.type.Tickable;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.BlockItem;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.type.Block;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.MetadataType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.MetadataTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

// needs to be fixed and icbf rn so its staying here for now
public class BlockDisplayEntityNew extends BlockDisplayBaseEntity implements Tickable {
    private static final DecimalFormat format = new DecimalFormat("#.###");
    private static final List<String> rendersAs2D = List.of(
            Items.CAMPFIRE.javaIdentifier(),
            Items.IRON_BARS.javaIdentifier(),
            Items.BELL.javaIdentifier()
    );
    private String animationExpression = "";
    private Vector3f rot;



    public BlockDisplayEntityNew(GeyserSession session, int entityId, long geyserId, UUID uuid, EntityDefinition<?> definition, Vector3f position, Vector3f motion, float yaw, float pitch, float headYaw) {
        super(session, entityId, geyserId, uuid, definition, position, motion, yaw, pitch, headYaw);
    }

    public static EntityDefinition<BlockDisplayEntityNew> buildEntityDef() {
        EntityDefinition<Entity> entityBase = EntityDefinition.builder(Entity::new)
                .addTranslator(MetadataTypes.BYTE, Entity::setFlags)
                .addTranslator(MetadataTypes.INT, Entity::setAir) // Air/bubbles
                .addTranslator(MetadataTypes.OPTIONAL_CHAT, Entity::setDisplayName)
                .addTranslator(MetadataTypes.BOOLEAN, Entity::setDisplayNameVisible)
                .addTranslator(MetadataTypes.BOOLEAN, Entity::setSilent)
                .addTranslator(MetadataTypes.BOOLEAN, Entity::setGravity)
                .addTranslator(MetadataTypes.POSE, (entity, entityMetadata) -> entity.setPose(entityMetadata.getValue()))
                .addTranslator(MetadataTypes.INT, Entity::setFreezing)
                .build();

        EntityDefinition<BlockDisplayBaseEntity> displayBase = EntityDefinition.inherited(BlockDisplayBaseEntity::new, entityBase)
                .addTranslator(null) // Interpolation delay
                .addTranslator(null) // Transformation interpolation duration
                .addTranslator(null) // Position/Rotation interpolation duration
                .addTranslator(MetadataTypes.VECTOR3, BlockDisplayBaseEntity::setTranslation) // Translation
                .addTranslator(MetadataTypes.VECTOR3, BlockDisplayBaseEntity::setScale) // Scale
                .addTranslator(MetadataTypes.QUATERNION, BlockDisplayBaseEntity::setLeftRotation) // Left rotation
                .addTranslator(MetadataTypes.QUATERNION, BlockDisplayBaseEntity::setRightRotation) // Right rotation
                .addTranslator(null) // Billboard render constraints
                .addTranslator(null) // Brightness override
                .addTranslator(null) // View range
                .addTranslator(null) // Shadow radius
                .addTranslator(null) // Shadow strength
                .addTranslator(null) // Width
                .addTranslator(null) // Height
                .addTranslator(null) // Glow color override
                .build();

        return EntityDefinition.inherited(BlockDisplayEntityNew::new, displayBase)
                .type(EntityType.BLOCK_DISPLAY)
                .identifier("minecraft:fox")
                .addTranslator(MetadataTypes.BLOCK_STATE, BlockDisplayEntityNew::setBlock)
                .build();
    }

    @Override
    public void spawnEntity() {
        position = position.add(this.getTranslationOffset());
        super.spawnEntity();
        dirtyMetadata.put(EntityDataTypes.COLLISION_BOX, Vector3f.ZERO);
        dirtyMetadata.put(EntityDataTypes.WIDTH, 0.0f);
        dirtyMetadata.put(EntityDataTypes.HEIGHT, 0.0f);

        setFlag(EntityFlag.SILENT, true);
        setFlag(EntityFlag.INVISIBLE, true);
        setFlag(EntityFlag.NO_AI, true);
        setFlag(EntityFlag.HAS_COLLISION, false);
        setFlag(EntityFlag.HAS_GRAVITY, false);
        setFlag(EntityFlag.PUSH_TOWARDS_CLOSEST_SPACE, false);

        updateBedrockMetadata();
    }

    @Override
    public void drawTick() {
        animationExpression = "";
        buildScale();
        buildTranslation();
        buildRotation();

        animate("animation.player.attack.positions", animationExpression.replace("=-0;", "=0;"), "ge:setvariable");
        // Reposition and Define FMBE Scale
        animate("animation.player.sleeping", "", "controller.animation.fox.move");
        animate("animation.creeper.swelling", "v.xbasepos=v.xbasepos??0;v.ybasepos=v.ybasepos??0;v.zbasepos=v.zbasepos??0;v.xpos=v.xpos??0;v.ypos=v.ypos??0;v.zpos=v.zpos??0;v.xrot=v.xrot??0;v.yrot=v.yrot??0;v.zrot=v.zrot??0;v.scale=v.scale??1;v.xzscale=v.xzscale??1;v.yscale=v.yscale??1;v.swelling_scale1=2.1385*math.sqrt(v.xzscale)*math.sqrt(v.scale);v.swelling_scale2=2.1385*math.sqrt(v.yscale)*math.sqrt(v.scale);", "ge:scale");
        animate("animation.ender_dragon.neck_head_movement", "v.head_rotation_x=0;v.head_rotation_y=0;v.head_rotation_z=0;v.head_position_x=(v.xbasepos*3741/8000)*math.sqrt(v.xzscale)*math.sqrt(v.scale);v.head_position_y=(10.6925+v.ybasepos*3741/8000)*math.sqrt(v.yscale)*math.sqrt(v.scale);v.head_position_z=(17.108-v.zbasepos*3741/8000)*math.sqrt(v.xzscale)*math.sqrt(v.scale);", "ge:shift_pos");

        // Define FMBE Rotation
        // X Axis
        animate("animation.warden.move", "v.body_x_rot=90+v.xrot;v.body_z_rot=90+v.yrot;", "ge:xrot");
        // Z Axis
        animate("animation.player.attack.rotations", "v.attack_body_rot_y=-v.zrot;", "ge:zrot");

        // Define FMBE Position
        // X Axis
        animate("animation.parrot.moving", "v.wing_flap=(16-v.xpos)/0.3;", "ge:xpos");
        animate("animation.minecart.move.v1.0", "v.rail_offset.x=0;v.rail_offset.y=1.6485+v.ypos/16;v.rail_offset.z=0;", "ge:ypos");
        animate("animation.parrot.dance", "v.dance.x=-v.zpos;v.dance.y=0;", "ge:zpos");
    }

    public void tick() {

    }

    private void animate(String anim, String stopExpression, String controller) {
        AnimateEntityPacket animation = new AnimateEntityPacket();
        animation.setAnimation(anim);
        animation.setNextState("none");
        animation.setBlendOutTime(0.0f);
        animation.setStopExpression(stopExpression);
        animation.setController(controller);
        animation.getRuntimeEntityIds().add(geyserId);
        session.sendUpstreamPacket(animation);
    }

    public void setBlock(IntEntityMetadata entityMetadata) {
        BlockState state = BlockState.of(entityMetadata.getPrimitiveValue());
        Block block = state.block();
        Item item = BlockItem.byBlock(block);
        if (item.javaIdentifier().contains("_door") || item.javaIdentifier().contains("_candle")) {
            setHand(Items.AIR.newItemStack(1, null).getItemStack());
            updateMainHand(session);
            return;
        }

        if (rendersAs2D.contains(block.javaIdentifier().toString())) {
            setHand(Items.AIR.newItemStack(1, null).getItemStack());
            updateMainHand(session);
            return;
        }
        setHand(item.newItemStack(1, null).getItemStack());
        updateMainHand(session);
    }

    private String mlVAR(String name, float variable) {
        return "v." + name + "=" + format.format(variable) + ";";
    }

    // everything below from here is complete fuckery to make this shit work with javas offset
    // god save you if you ever stumble upon this code because it is FUCKED

    private String mlVAR(String name, double variable) {
        return "v." + name + "=" + format.format(variable) + ";";
    }

    @Override
    public void moveAbsolute(Vector3f position, float yaw, float pitch, float headYaw, boolean isOnGround, boolean teleported) {
        //super.moveAbsolute(position, yaw, pitch, headYaw, isOnGround, teleported);
        this.position = position;
        moveAbsoluteImmediate(position.add(getTranslationOffsetSub()), yaw, pitch, headYaw, isOnGround, teleported);
    }

    protected void moveAbsoluteImmediate(Vector3f position) {
        this.moveAbsoluteImmediate(position, yaw, pitch, headYaw, isOnGround(), false);
    }

    protected void moveAbsoluteImmediate(Vector3f position, float yaw, float pitch, float headYaw, boolean isOnGround, boolean teleported) {
        MoveEntityDeltaPacket moveEntityDeltaPacket = new MoveEntityDeltaPacket();
        moveEntityDeltaPacket.setRuntimeEntityId(geyserId);

        if (isOnGround) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.ON_GROUND);
        }
        setOnGround(isOnGround);

        if (teleported) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.TELEPORTING);
        }

        if (this.position.getX() != position.getX()) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.HAS_X);
            moveEntityDeltaPacket.setX(position.getX());
        }
        if (this.position.getY() != position.getY()) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.HAS_Y);
            moveEntityDeltaPacket.setY(position.getY());
        }
        if (this.position.getZ() != position.getZ()) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.HAS_Z);
            moveEntityDeltaPacket.setZ(position.getZ());
        }
        setPosition(position);

        if (getYaw() != yaw) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.HAS_YAW);
            moveEntityDeltaPacket.setYaw(yaw);
            setYaw(yaw);
        }
        if (getPitch() != pitch) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.HAS_PITCH);
            moveEntityDeltaPacket.setPitch(pitch);
            setPitch(pitch);
        }
        if (getHeadYaw() != headYaw) {
            moveEntityDeltaPacket.getFlags().add(MoveEntityDeltaPacket.Flag.HAS_HEAD_YAW);
            moveEntityDeltaPacket.setHeadYaw(headYaw);
            setHeadYaw(headYaw);
        }

        if (!moveEntityDeltaPacket.getFlags().isEmpty()) {
            session.sendUpstreamPacket(moveEntityDeltaPacket);
        }
    }

    @Override
    public void moveRelative(double relX, double relY, double relZ, float yaw, float pitch, float headYaw, boolean isOnGround) {
        //super.moveAbsolute(position, yaw, pitch, headYaw, isOnGround, teleported);
        moveAbsoluteImmediate(this.position.add(relX, relY, relZ));
    }

    private void buildTranslation() {
        // * 16 bc fmbe method makes block pos multiples of 16

        Vector3f t = getTranslationOffset();
        Vector3f s = getScale();

        //animationExpression += mlVAR("xpos", (t.getX()+(s.getX())) * 16) + mlVAR("ypos", (t.getY()+(s.getY())) * 16) + mlVAR("zpos", (t.getZ()+(s.getZ())) * 16);
        animationExpression += mlVAR("xpos", 0) + mlVAR("ypos", (-s.getY()) * 16) + mlVAR("zpos", 0);

        animationExpression += mlVAR("xbasepos", (0.5) * 16) + mlVAR("ybasepos", (0.5) * 16) + mlVAR("zbasepos", (0.5) * 16);

    }

    private Vector3f getTranslationOffset() {
        return this.getTranslation().sub(0, getScale().getY() - 1, 0);
    }

    private Vector3f getTranslationOffsetSub() {
        return this.getTranslation().add(0, getScale().getY() - 1, 0);
    }

    public void buildScale() {
        float xzscale = Math.max(getScale().getX(), getScale().getZ());


        animationExpression += mlVAR("xzscale", xzscale) + mlVAR("yscale", getScale().getY());
    }

    // this is going to make me lose my mind :D
    public void buildRotation() {
        Vector4f q = getLeftRotation();
        q = Vector4f.from(MathUtils.clampOne(q.getX()), MathUtils.clampOne(q.getY()), MathUtils.clampOne(q.getZ()), MathUtils.clampOne(q.getW()));
        rot = MathUtils.toEuler(q);
        rot = Vector3f.from(Math.toDegrees(rot.getX()), Math.toDegrees(rot.getY()), Math.toDegrees(rot.getZ()));


        animationExpression += mlVAR("xrot", rot.getX()) + mlVAR("yrot", -rot.getY()) + mlVAR("zrot", -rot.getZ());

    }

}
