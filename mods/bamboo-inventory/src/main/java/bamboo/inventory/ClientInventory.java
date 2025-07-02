package bamboo.inventory;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

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

        registerKey("ctrl+left", MoveAction::moveStacks);
        registerKey("ctrl+right", MoveAction::moveStacksLeaveOne);

        registerKey("alt+left", MoveAction::moveAll);
        registerKey("alt+right", MoveAction::dropStacks);

        registerKey("ctrl+scroll", MoveAction::craftOrBuyOne, false);

        registerKey("r", MergeAction::merge, false);
    }

    private static void registerKey(String key, InventoryHandler handler, boolean cancelOnRelease) {
        Client.registerKey(key, handler);
        if (cancelOnRelease) {
            Client.registerKey(key, (InventoryHandler) ClientInventory::cancel, true);
        }
    }

    private static void registerKey(String key, InventoryHandler handler) {
        registerKey(key, handler, true);
    }

    private static void cancel(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
    }
}
