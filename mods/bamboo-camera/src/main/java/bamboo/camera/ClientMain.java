package bamboo.camera;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.keybinding.Key;
import bamboo.lib.lifecycle.MinecraftClientLifecycle;

public class ClientMain implements ClientModInitializer {
    public void onInitializeClient() {
        Key.parse("v").execute(client -> {
            if (client.currentScreen == null) {
                Camera.toggle();
                return true;
            }
            return false;
        });

        Camera.init();
        MinecraftClientLifecycle.onJoinWorld(Camera::onJoinWorld);
    }
}
