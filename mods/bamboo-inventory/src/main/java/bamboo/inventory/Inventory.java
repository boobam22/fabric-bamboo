package bamboo.inventory;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.lib.config.ConfigEntry;

public class Inventory implements ModInitializer {
    public static ConfigEntry<Boolean> villagerMaxBooks = Server.registerConfig("inventory.villagerMaxBooks", false);

    @Override
    public void onInitialize() {
    }
}
