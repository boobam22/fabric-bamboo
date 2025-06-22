package bamboo.camera;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class Camera {
    public static final Camera INSTANCE = new Camera();
    private boolean originChunkCullingEnabled;
    private Entity originCameraEntity;
    private CameraEntity cameraEntity;

    private Camera() {
    }

    public void toggle() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (isActive()) {
            client.setCameraEntity(originCameraEntity);
            client.chunkCullingEnabled = originChunkCullingEnabled;
        } else {
            originCameraEntity = client.getCameraEntity();
            originChunkCullingEnabled = client.chunkCullingEnabled;

            if (cameraEntity == null) {
                cameraEntity = new CameraEntity(client.player.clientWorld);
            }

            cameraEntity.refreshPositionAndAngles(originCameraEntity);
            client.setCameraEntity(cameraEntity);
            client.chunkCullingEnabled = false;
        }
    }

    public boolean isActive() {
        return cameraEntity == MinecraftClient.getInstance().getCameraEntity();
    }
}
