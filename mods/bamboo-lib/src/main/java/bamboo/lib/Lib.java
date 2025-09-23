package bamboo.lib;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.config.ConfigRegistry;
import bamboo.lib.command.Command;
import bamboo.lib.command.ConfigCommand;

public class Lib implements ModInitializer {
    public static final ConfigRegistry configRegistry = new ConfigRegistry("bamboo-server.properties");
    public static final Registry<Command> commandRegistry = new Registry<>();
    public static final Registry<Runnable> startHandlers = new Registry<>();
    public static final Registry<Runnable> shutdownHandlers = new Registry<>();

    @Override
    public void onInitialize() {
        startHandlers.register(configRegistry::loadConfig);
        shutdownHandlers.register(configRegistry::saveConfig);
        commandRegistry.register(new ConfigCommand());
    }
}
