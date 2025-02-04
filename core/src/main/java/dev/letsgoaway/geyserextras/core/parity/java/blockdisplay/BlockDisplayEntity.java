package dev.letsgoaway.geyserextras.core.parity.java.blockdisplay;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AnimateEntityPacket;
import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.entity.type.Tickable;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.MetadataType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponents;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class BlockDisplayEntity extends BlockDisplayBaseEntity implements Tickable {
    private static final DecimalFormat format = new DecimalFormat("#.###");
    public BlockDefinition bedrockBlockId;
    private String animationExpression = "";

    public BlockDisplayEntity(GeyserSession session, int entityId, long geyserId, UUID uuid, EntityDefinition<?> definition, Vector3f position, Vector3f motion, float yaw, float pitch, float headYaw) {
        super(session, entityId, geyserId, uuid, definition, position, motion, yaw, pitch, headYaw);
    }

    public static EntityDefinition<BlockDisplayEntity> buildEntityDef() {
        EntityDefinition<Entity> entityBase = EntityDefinition.builder(Entity::new)
                .addTranslator(MetadataType.BYTE, Entity::setFlags)
                .addTranslator(MetadataType.INT, Entity::setAir) // Air/bubbles
                .addTranslator(MetadataType.OPTIONAL_CHAT, Entity::setDisplayName)
                .addTranslator(MetadataType.BOOLEAN, Entity::setDisplayNameVisible)
                .addTranslator(MetadataType.BOOLEAN, Entity::setSilent)
                .addTranslator(MetadataType.BOOLEAN, Entity::setGravity)
                .addTranslator(MetadataType.POSE, (entity, entityMetadata) -> entity.setPose(entityMetadata.getValue()))
                .addTranslator(MetadataType.INT, Entity::setFreezing)
                .build();

        EntityDefinition<BlockDisplayBaseEntity> displayBase = EntityDefinition.inherited(BlockDisplayBaseEntity::new, entityBase)
                .addTranslator(null) // Interpolation delay
                .addTranslator(null) // Transformation interpolation duration
                .addTranslator(null) // Position/Rotation interpolation duration
                .addTranslator(MetadataType.VECTOR3, BlockDisplayBaseEntity::setTranslation) // Translation
                .addTranslator(MetadataType.VECTOR3, BlockDisplayBaseEntity::setScale) // Scale
                .addTranslator(MetadataType.QUATERNION, BlockDisplayBaseEntity::setLeftRotation) // Left rotation
                .addTranslator(MetadataType.QUATERNION, BlockDisplayBaseEntity::setRightRotation) // Right rotation
                .addTranslator(null) // Billboard render constraints
                .addTranslator(null) // Brightness override
                .addTranslator(null) // View range
                .addTranslator(null) // Shadow radius
                .addTranslator(null) // Shadow strength
                .addTranslator(null) // Width
                .addTranslator(null) // Height
                .addTranslator(null) // Glow color override
                .build();

        return EntityDefinition.inherited(BlockDisplayEntity::new, displayBase)
                .type(EntityType.BLOCK_DISPLAY)
                .identifier("minecraft:fox")
                .addTranslator(MetadataType.BLOCK_STATE, BlockDisplayEntity::setBlock)
                .build();
    }

    @Override
    public void spawnEntity() {
        position = position.add(this.getTranslation());
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
    }

    @Override
    public void drawTick() {

    }

    public void tick() {
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
        bedrockBlockId = session.getBlockMappings().getBedrockBlock(entityMetadata.getPrimitiveValue());

        setHand(Items.ACACIA_LEAVES.newItemStack(1, new DataComponents(new HashMap<>())).getItemStack());
        updateMainHand(session);
    }

    private String mlVAR(String name, float variable) {
        return "v." + name + "=" + format.format(variable) + ";";
    }

    private String mlVAR(String name, double variable) {
        return "v." + name + "=" + format.format(variable) + ";";
    }

    // everything below from here is complete fuckery to make this shit work with javas offset
    // god save you if you ever stumble upon this code because it is FUCKED

    @Override
    public void moveAbsolute(Vector3f position, float yaw, float pitch, float headYaw, boolean isOnGround, boolean teleported) {
        //super.moveAbsolute(position, yaw, pitch, headYaw, isOnGround, teleported);

        super.moveAbsolute(position.add(-0.5, 0, -0.5), yaw, pitch, headYaw, isOnGround, teleported);
        this.position = position;
    }

    @Override
    public void moveRelative(double relX, double relY, double relZ, float yaw, float pitch, float headYaw, boolean isOnGround) {
        //super.moveAbsolute(position, yaw, pitch, headYaw, isOnGround, teleported);

        super.moveRelative(relX - 0.5, relY, relZ - 0.5, yaw, pitch, headYaw, isOnGround);
        this.position = position.add(0.5, 0, 0.5);
    }

    private void buildTranslation() {
    //    moveRelative(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch, headYaw, false);
        //    position = position.sub(pos);

        // * 16 bc fmbe method makes block pos multiples of 16
        animationExpression += mlVAR("xpos", (-0.5 + getTranslation().getX()) * 16) + mlVAR("ypos", (0.5 + getTranslation().getY()) * 16) + mlVAR("zpos", (-0.5 + getTranslation().getZ()) * 16);
        animationExpression += mlVAR("xbasepos", (0.5) * 16) + mlVAR("ybasepos", (0.5) * 16) + mlVAR("zbasepos", (0.5) * 16);

    }


    public void buildScale() {
        float xzscale = Math.max(getScale().getX(), getScale().getZ());


        animationExpression += mlVAR("xzscale", xzscale) + mlVAR("yscale", getScale().getY());
    }

    // this is going to make me lose my mind :D
    public void buildRotation() {
        Vector4f q = getLeftRotation();

        double sinr_cosp = 2 * (q.getW() * q.getX() + q.getY() * q.getZ());
        double cosr_cosp = 1 - 2 * (q.getX() * q.getX() + q.getY() * q.getY());
        double roll = Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        double sinp = Math.sqrt(1 + 2 * (q.getW() * q.getY() - q.getX() * q.getZ()));
        double cosp = Math.sqrt(1 - 2 * (q.getW() * q.getY() - q.getX() * q.getZ()));
        double pitch = 2 * Math.atan2(sinp, cosp) - Math.PI / 2;

        // yaw (z-axis rotation)
        double siny_cosp = 2 * (q.getW() * q.getZ() + q.getX() * q.getY());
        double cosy_cosp = 1 - 2 * (q.getY() * q.getY() + q.getZ() * q.getZ());
        double yaw = Math.atan2(siny_cosp, cosy_cosp);


        animationExpression += mlVAR("xrot", (roll) * (180 / Math.PI)) + mlVAR("yrot", (-pitch) * (180 / Math.PI)) + mlVAR("zrot", (-yaw) * (180 / Math.PI));
        //     GeyserExtras.SERVER.log(animationExpression);
    }

}
