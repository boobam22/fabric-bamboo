package bamboo.camera;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.PlayerInput;

public class Camera {
    private static boolean originChunkCullingEnabled;
    private static Entity originCameraEntity;
    private static CameraEntity cameraEntity;

    public static void toggle() {
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

    public static boolean isActive() {
        return cameraEntity == MinecraftClient.getInstance().getCameraEntity();
    }

    public static void handleInput(PlayerInput input) {
    }

    public static void handleMouse(double x, double y) {
        cameraEntity.changeLookDirection(x, y);
    }
}
