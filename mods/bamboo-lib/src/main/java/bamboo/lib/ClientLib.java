package bamboo.lib;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigRegistry;
import bamboo.lib.keybinding.IngameHandler;

public class ClientLib implements ClientModInitializer {
    public static final ConfigRegistry configRegistry = new ConfigRegistry("bamboo.properties");
    public static final Registry<Runnable> joinWorldHandlers = new Registry<>();
    public static final Registry<Runnable> exitWorldHandlers = new Registry<>();
    public static final Registry<Runnable> exitGameHandlers = new Registry<>();

    @Override
    public void onInitializeClient() {
        joinWorldHandlers.register(configRegistry::loadConfig);
        exitWorldHandlers.register(configRegistry::saveConfig);

        Client.registerKey("b+c", reloadConfig);
    }

    private IngameHandler reloadConfig = client -> {
        configRegistry.loadConfig();
        Client.message("Config reloaded");
        return true;
    };
}
