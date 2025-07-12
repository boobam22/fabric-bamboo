package bamboo.camera;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.keybinding.IngameHandler;

public class ClientCamera implements ClientModInitializer {
    public static CameraController cameraController = new CameraController();

    @Override
    public void onInitializeClient() {
        Client.registerKey("v", (IngameHandler) cameraController::toggle);
        Client.onExitWorld(() -> {
            if (cameraController.isActive()) {
                cameraController.toggle();
            }
        });
    }
}
