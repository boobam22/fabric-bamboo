package bamboo.camera;

import net.minecraft.entity.Entity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;

public class CameraController {
    private static boolean originChunkCullingEnabled = MinecraftClient.getInstance().chunkCullingEnabled;;
    private Entity originCameraEntity;
    private CameraEntity cameraEntity;

    public boolean isActive() {
        return cameraEntity != null && cameraEntity == MinecraftClient.getInstance().getCameraEntity();
    }

    public void toggle(MinecraftClient client) {
        if (isActive()) {
            client.setCameraEntity(originCameraEntity);
            client.chunkCullingEnabled = originChunkCullingEnabled;
        } else {
            originCameraEntity = client.getCameraEntity();
            cameraEntity = new CameraEntity(client.world);
            cameraEntity.refreshPositionAndAngles(originCameraEntity);
            client.setCameraEntity(cameraEntity);
            client.chunkCullingEnabled = false;
        }
    }

    public void toggle() {
        toggle(MinecraftClient.getInstance());
    }

    public void handleInput(PlayerInput input) {
        double x = (input.left() ? 1 : 0) + (input.right() ? -1 : 0);
        double y = (input.jump() ? 1 : 0) + (input.sneak() ? -1 : 0);
        double z = (input.forward() ? 1 : 0) + (input.backward() ? -1 : 0);
        double f = input.sprint() ? 5 : 0.8;
        float yaw = cameraEntity.getYaw() * (float) Math.PI / 180;
        Vec3d offset = new Vec3d(x, y, z).rotateY(-yaw).multiply(f);

        cameraEntity.resetPosition();
        cameraEntity.move(null, offset);
    }

    public void handleMouse(double x, double y) {
        cameraEntity.changeLookDirection(x, y);
    }
}