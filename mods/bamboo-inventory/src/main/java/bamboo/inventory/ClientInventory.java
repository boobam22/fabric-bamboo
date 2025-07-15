package bamboo.inventory;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.inventory.action.MoveAction;
import bamboo.inventory.action.MergeAction;

public class ClientInventory implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerKey("shift+left", MoveAction::moveOneStack);
        registerKey("shift+left+move", MoveAction::moveOneStack);
        registerKey("shift+right", MoveAction::moveOneStackLeaveOne);
        registerKey("shift+right+move", MoveAction::moveOneStackLeaveOne);

        registerKey("alt+left", MoveAction::moveStacks);
        registerKey("alt+right", MoveAction::moveStacksLeaveOne);

        registerKey("ctrl+left", MoveAction::moveAll);
        registerKey("ctrl+right", MoveAction::dropStacks);

        registerKey("alt+scroll", MoveAction::craftOrBuyOne, false);

        registerKey("r", MergeAction::merge, false);
    }

    private static InventoryHandler cancel = (handler, slots, focusedSlot) -> {
    };

    private static void registerKey(String key, InventoryHandler handler, boolean cancelOnRelease) {
        Client.registerKey(key, handler);
        if (cancelOnRelease) {
            Client.registerKey(key, cancel, true);
        }
    }

    private static void registerKey(String key, InventoryHandler handler) {
        registerKey(key, handler, true);
    }
}
