package bamboo.place;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.keybinding.IngameHandler;

public class ClientPlace implements ClientModInitializer {
    public static ConfigEntry<Boolean> fastUse = Client.registerConfig("place.fastUse", false);

    @Override
    public void onInitializeClient() {
        Client.registerKey("b+p", toggleFastUse);
    }

    private static IngameHandler toggleFastUse = client -> {
        fastUse.set(!fastUse.getValue());
        Client.message("Fast Use [%s]", fastUse.getValue());
        return true;
    };
}
