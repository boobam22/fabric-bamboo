package bamboo.lib;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.config.ConfigManager;
import bamboo.lib.lifecycle.MinecraftClientLifecycle;

public class Lib implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigManager.loadConfig();
        MinecraftClientLifecycle.onExitWorld(ConfigManager::saveConfig);
    }
}
