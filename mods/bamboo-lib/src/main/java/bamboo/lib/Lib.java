package bamboo.lib;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.config.ConfigManager;
import bamboo.lib.command.Command;

public class Lib implements ModInitializer {
    public static Registry<Command> commandRegistry = new Registry<>();

    @Override
    public void onInitialize() {
        ConfigManager.loadConfig();
    }
}
