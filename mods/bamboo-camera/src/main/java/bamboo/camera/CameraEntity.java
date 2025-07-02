package bamboo.camera;

import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;

public class CameraEntity extends MarkerEntity {
    private MinecraftClient client;
    private boolean originChunkCullingEnabled;
    private Entity originCameraEntity;

    public CameraEntity(MinecraftClient client) {
        super(EntityType.MARKER, client.world);
        this.calculateDimensions();

        this.client = client;
        this.originChunkCullingEnabled = client.chunkCullingEnabled;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityType.PLAYER.getDimensions();
    }

    public void refreshPositionAndAngles(Entity entity) {
        this.refreshPositionAndAngles(entity.getPos(), entity.getYaw(), entity.getPitch());
    }

    public boolean isActive() {
        return this == client.getCameraEntity();
    }

    public void toggle() {
        if (isActive()) {
            client.setCameraEntity(originCameraEntity);
            client.chunkCullingEnabled = originChunkCullingEnabled;
        } else {
            originCameraEntity = client.getCameraEntity();
            refreshPositionAndAngles(originCameraEntity);
            client.setCameraEntity(this);
            client.chunkCullingEnabled = false;
        }
    }

    public void handleInput(PlayerInput input) {
        double x = (input.left() ? 1 : 0) + (input.right() ? -1 : 0);
        double y = (input.jump() ? 1 : 0) + (input.sneak() ? -1 : 0);
        double z = (input.forward() ? 1 : 0) + (input.backward() ? -1 : 0);
        double f = input.sprint() ? 5 : 0.8;
        float yaw = this.getYaw() * (float) Math.PI / 180;
        Vec3d offset = new Vec3d(x, y, z).rotateY(-yaw).multiply(f);

        this.resetPosition();
        this.move(null, offset);
    }

    public void handleMouse(double x, double y) {
        this.changeLookDirection(x, y);
    }
}