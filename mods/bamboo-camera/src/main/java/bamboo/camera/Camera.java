package bamboo.camera;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MarkerEntity;

public class Camera {
    public static final Camera INSTANCE = new Camera();
    private boolean originChunkCullingEnabled;
    private Entity originCameraEntity;
    private MarkerEntity cameraEntity;

    private Camera() {
    }

    public void toggle() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (isActive()) {
            client.setCameraEntity(originCameraEntity);
            client.chunkCullingEnabled = originChunkCullingEnabled;

            cameraEntity.remove(RemovalReason.KILLED);
        } else {
            originCameraEntity = client.getCameraEntity();
            originChunkCullingEnabled = client.chunkCullingEnabled;

            cameraEntity = new MarkerEntity(EntityType.MARKER, client.player.clientWorld);
            cameraEntity.setPosition(originCameraEntity.getEyePos());
            cameraEntity.setAngles(originCameraEntity.getYaw(), originCameraEntity.getPitch());
            client.player.clientWorld.addEntity(cameraEntity);

            client.setCameraEntity(cameraEntity);
            client.chunkCullingEnabled = false;
        }
    }

    public boolean isActive() {
        return cameraEntity == MinecraftClient.getInstance().getCameraEntity();
    }
}
