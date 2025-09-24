package bamboo.inventory;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.keybinding.IngameHandler;
import bamboo.inventory.action.MoveAction;
import bamboo.inventory.action.MergeAction;

public class ClientInventory implements ClientModInitializer {
    public static ConfigEntry<Boolean> handRestock = Client.registerConfig("inventory.handRestock", false);

    @Override
    public void onInitializeClient() {
        registerKey("shift+left", MoveAction::moveOneStack);
        registerKey("shift+left+move", MoveAction::moveOneStack);
        registerKey("shift+right", MoveAction::moveOneStackLeaveOne);
        registerKey("shift+right+move", MoveAction::moveOneStackLeaveOne);

        registerKey("alt+left", MoveAction::moveStacks);
        registerKey("alt+right", MoveAction::moveStacksLeaveOne);

        registerKey("ctrl+left", MoveAction::moveAll);
        registerKey("ctrl+right", MoveAction::dropStacks, false);

        registerKey("alt+scroll", MoveAction::craftOne, false);
        registerKey("alt+scroll", MoveAction::buyOne, false);

        registerKey("r", MergeAction::merge, false);

        Client.registerKey("b+r", toggleHandRestock);
    }

    private static void registerKey(String key, InventoryHandler handler, boolean cancelOnRelease) {
        InventoryHandler cancel = (slots, focusedSlot) -> true;

        Client.registerKey(key, handler);
        if (cancelOnRelease) {
            Client.registerKey(key, cancel, true);
        }
    }

    private static void registerKey(String key, InventoryHandler handler) {
        registerKey(key, handler, true);
    }

    private static IngameHandler toggleHandRestock = client -> {
        handRestock.toggle();
        Client.message("Hand Restock [%s]", handRestock.getValue());
        return true;
    };
}
