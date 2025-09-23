package bamboo.place;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.keybinding.IngameHandler;

public class ClientPlace implements ClientModInitializer {
    public static ConfigEntry<Boolean> useCooldown = Client.registerConfig("place.useCooldown", true);
    public static ConfigEntry<Boolean> fastUse = Client.registerConfig("place.fastUse", false);
    public static ConfigEntry<Integer> usePerTick = Client.registerConfig("place.usePerTick", 16, 1, 64);

    @Override
    public void onInitializeClient() {
        Client.registerKey("b+p", toggleUseCooldown);
        Client.registerKey("b+f", toggleFastUse);
    }

    private static IngameHandler toggleUseCooldown = client -> {
        useCooldown.toggle();
        Client.message("Use Cooldown [%s]", useCooldown.getValue());
        return true;
    };

    private static IngameHandler toggleFastUse = client -> {
        fastUse.toggle();
        Client.message("Fast Use [%s]", fastUse.getValue());
        return true;
    };
}
