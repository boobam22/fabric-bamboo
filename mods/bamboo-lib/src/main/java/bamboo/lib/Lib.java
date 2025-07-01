package bamboo.lib;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.config.ConfigRegistry;
import bamboo.lib.command.Command;

public class Lib implements ModInitializer {
    public static final ConfigRegistry ConfigRegistry = new ConfigRegistry();
    public static final Registry<Command> commandRegistry = new Registry<>();

    @Override
    public void onInitialize() {
    }
}
