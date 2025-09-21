package bamboo.place;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.lib.config.ConfigEntry;

public class Place implements ModInitializer {
    public static ConfigEntry<Integer> fastUseDistance = Server.registerConfig("place.fastUseDistance", 16);

    @Override
    public void onInitialize() {
    }
}
