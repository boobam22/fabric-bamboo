package bamboo.camera;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.api.Client;
import bamboo.lib.keybinding.handler.IngameHandler;

public class ClientCamera implements ClientModInitializer {
    private static CameraEntity cameraEntity;

    @Override
    public void onInitializeClient() {
        IngameHandler toggle = client -> cameraEntity.toggle();
        Client.registerKey("v", toggle);
        Client.onJoinWorld(() -> {
            cameraEntity = new CameraEntity(MinecraftClient.getInstance());
        });
    }
}
