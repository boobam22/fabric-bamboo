package bamboo.camera;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;

import bamboo.lib.api.Client;
import bamboo.lib.keybinding.handler.IngameHandler;

public class ClientCamera implements ClientModInitializer {
    private static boolean originChunkCullingEnabled;
    private static Entity originCameraEntity;
    private static CameraEntity cameraEntity;

    @Override
    public void onInitializeClient() {
        MinecraftClient client = MinecraftClient.getInstance();
        originChunkCullingEnabled = client.chunkCullingEnabled;

        Client.registerKey("v", (IngameHandler) this::toggle);
        Client.onExitWorld(() -> {
            client.chunkCullingEnabled = originChunkCullingEnabled;
        });
    }

    public void toggle(MinecraftClient client) {
        if (isActive()) {
            client.setCameraEntity(originCameraEntity);
            client.chunkCullingEnabled = originChunkCullingEnabled;
        } else {
            originCameraEntity = client.getCameraEntity();
            cameraEntity = new CameraEntity(client.player.clientWorld);
            cameraEntity.refreshPositionAndAngles(originCameraEntity);
            client.setCameraEntity(cameraEntity);
            client.chunkCullingEnabled = false;
        }
    }

    public static boolean isActive() {
        return cameraEntity != null && cameraEntity == MinecraftClient.getInstance().getCameraEntity();
    }

    public static void handleInput(PlayerInput input) {
        double x = (input.left() ? 1 : 0) + (input.right() ? -1 : 0);
        double y = (input.jump() ? 1 : 0) + (input.sneak() ? -1 : 0);
        double z = (input.forward() ? 1 : 0) + (input.backward() ? -1 : 0);
        double f = input.sprint() ? 5 : 0.8;

        float yaw = cameraEntity.getYaw() * (float) Math.PI / 180;
        Vec3d offset = new Vec3d(x, y, z).rotateY(-yaw).multiply(f);

        cameraEntity.resetPosition();
        cameraEntity.move(null, offset);
    }

    public static void handleMouse(double x, double y) {
        cameraEntity.changeLookDirection(x, y);
    }
}
