package bamboo.place;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.lib.config.ConfigEntry;

public class Place implements ModInitializer {
    public static ConfigEntry<Integer> scaffoldingDistance = Server.registerConfig("place.scaffoldingDistance", 16);

    @Override
    public void onInitialize() {
    }
}
