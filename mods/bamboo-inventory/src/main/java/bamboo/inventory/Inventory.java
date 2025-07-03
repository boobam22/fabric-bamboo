package bamboo.inventory;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.inventory.command.InventoryCommand;

public class Inventory implements ModInitializer {
    @Override
    public void onInitialize() {
        Server.registerCommand(new InventoryCommand());
    }
}