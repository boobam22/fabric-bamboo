package bamboo.inventory;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.lib.config.ConfigEntry;

public class Inventory implements ModInitializer {
    public static ConfigEntry<Boolean> villagerMaxBooks = Server.registerConfig("inventory.villagerMaxBooks", false);
    public static ConfigEntry<Boolean> disableTradeLock = Server.registerConfig("inventory.disableTradeLock", false);

    @Override
    public void onInitialize() {
    }
}
