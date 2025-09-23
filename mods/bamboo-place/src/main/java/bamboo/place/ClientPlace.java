package bamboo.place;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.keybinding.IngameHandler;

public class ClientPlace extends Place implements ClientModInitializer {
    public static ConfigEntry<Boolean> useCooldown = Client.registerConfig("place.useCooldown", true);

    @Override
    public void onInitializeClient() {
        Client.registerKey("b+p", toggleFastUse);
    }

    private static IngameHandler toggleFastUse = client -> {
        useCooldown.set(!useCooldown.getValue());
        Client.message("Use Cooldown [%s]", useCooldown.getValue());
        return true;
    };
}
