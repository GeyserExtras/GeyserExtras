package dev.letsgoaway.geyserextras.core.parity.java.blockdisplay;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.imaginary.Quaternionf;
import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.entity.spawn.EntitySpawnContext;
import org.geysermc.geyser.entity.type.living.animal.FoxEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;

import java.util.UUID;

public class BlockDisplayBaseEntity extends FoxEntity {

    private Vector3f baseTranslation = Vector3f.ZERO;
    private Vector3f blockDisplayScale = Vector3f.ONE;

    private Quaternionf leftRotation = Quaternionf.from(0, 0, 0, 1);
    private Quaternionf rightRotation = Quaternionf.from(0, 0, 0, 1);

    public BlockDisplayBaseEntity(EntitySpawnContext context) {
        super(context);
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

    public Quaternionf getLeftRotation() {
        return this.leftRotation;
    }

    public void setLeftRotation(EntityMetadata<Quaternionf, ?> translationMeta) {
        if (translationMeta.getValue() == null) {
            this.leftRotation = Quaternionf.from(0, 0, 0, 1);
            return;
        }
        this.leftRotation = translationMeta.getValue();
    }

    public Quaternionf getRightRotation() {
        return this.rightRotation;
    }

    public void setRightRotation(EntityMetadata<Quaternionf, ?> translationMeta) {
        if (translationMeta.getValue() == null) {
            this.rightRotation = Quaternionf.from(0, 0, 0, 1);
            return;
        }
        this.rightRotation = translationMeta.getValue();
    }

}
