package bamboo.camera;

import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.client.world.ClientWorld;

public class CameraEntity extends MarkerEntity {
    public CameraEntity(ClientWorld world) {
        super(EntityType.MARKER, world);
        this.calculateDimensions();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityType.PLAYER.getDimensions();
    }

    public void refreshPositionAndAngles(Entity entity) {
        this.refreshPositionAndAngles(entity.getPos(), entity.getYaw(), entity.getPitch());
    }
}