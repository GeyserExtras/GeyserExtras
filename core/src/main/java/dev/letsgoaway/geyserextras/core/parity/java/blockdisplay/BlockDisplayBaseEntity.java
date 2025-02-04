package dev.letsgoaway.geyserextras.core.parity.java.blockdisplay;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector4f;
import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.entity.type.living.animal.FoxEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;

import java.util.UUID;

public class BlockDisplayBaseEntity extends FoxEntity {

    private Vector3f baseTranslation = Vector3f.ZERO;
    private Vector3f blockDisplayScale = Vector3f.ONE;

    private Vector4f leftRotation = Vector4f.ZERO;
    private Vector4f rightRotation = Vector4f.ZERO;

    public BlockDisplayBaseEntity(GeyserSession session, int entityId, long geyserId, UUID uuid, EntityDefinition<?> definition, Vector3f position, Vector3f motion, float yaw, float pitch, float headYaw) {
        super(session, entityId, geyserId, uuid, definition, position, motion, yaw, pitch, headYaw);
    }

    public Vector3f getTranslation() {
        return baseTranslation;
    }

    public void setTranslation(EntityMetadata<Vector3f, ?> translationMeta) {
        if (translationMeta.getValue() == null) {
            return;
        }
        this.baseTranslation = translationMeta.getValue();
    }

    public Vector3f getScale() {
        return this.blockDisplayScale;
    }

    public void setScale(EntityMetadata<Vector3f, ?> translationMeta) {
        if (translationMeta.getValue() == null) {
            return;
        }
        this.blockDisplayScale = translationMeta.getValue();
    }


    public void setLeftRotation(EntityMetadata<Vector4f, ?> translationMeta) {
        if (translationMeta.getValue() == null) {
            return;
        }
        this.leftRotation = translationMeta.getValue();
    }
    public Vector4f getLeftRotation() {
        return this.leftRotation;
    }

    public void setRightRotation(EntityMetadata<Vector4f, ?> translationMeta) {
        if (translationMeta.getValue() == null) {
            return;
        }
        this.rightRotation = translationMeta.getValue();
    }
    public Vector4f getRightRotation() {
        return this.rightRotation;
    }

}
