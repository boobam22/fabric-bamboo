package bamboo.lib;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.lifecycle.MinecraftClientLifecycle;

public class ClientLib extends Lib implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MinecraftClientLifecycle.onExitWorld(() -> getConfigRegistry().saveConfig());
    }
}
