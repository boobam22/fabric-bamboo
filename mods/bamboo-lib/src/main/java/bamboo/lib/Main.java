package bamboo.lib;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.config.ConfigManager;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigManager.loadConfig();
    }
}
