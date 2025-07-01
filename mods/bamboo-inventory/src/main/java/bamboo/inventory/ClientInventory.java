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
        InventoryHandler cancel = (a, b, c) -> {
        };

        Client.registerKey("shift+left", (InventoryHandler) MoveAction::moveOneStack);
        Client.registerKey("shift+left", cancel, true);
        Client.registerKey("shift+left+move", (InventoryHandler) MoveAction::moveOneStack);
        Client.registerKey("shift+right", (InventoryHandler) MoveAction::moveOneStackLeaveOne);
        Client.registerKey("shift+right", cancel, true);
        Client.registerKey("shift+right+move", (InventoryHandler) MoveAction::moveOneStackLeaveOne);

        Client.registerKey("ctrl+left", (InventoryHandler) MoveAction::moveStacks);
        Client.registerKey("ctrl+left", cancel, true);
        Client.registerKey("ctrl+right", (InventoryHandler) MoveAction::moveStacksLeaveOne);
        Client.registerKey("ctrl+right", cancel, true);

        Client.registerKey("alt+left", (InventoryHandler) MoveAction::moveAll);
        Client.registerKey("alt+left", cancel, true);

        Client.registerKey("alt+right", (InventoryHandler) MoveAction::dropStacks);
        Client.registerKey("alt+right", cancel, true);

        Client.registerKey("ctrl+scroll", (InventoryHandler) MoveAction::craftOrBuyOne);

        Client.registerKey("r", (InventoryHandler) MergeAction::merge);
    }

    public static void info(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        System.out.println(handler.getClass());
        if (focusedSlot != null) {
            System.out.println(focusedSlot.getClass() + "  " + focusedSlot.id);
            System.out.println(focusedSlot.inventory.getClass() + "  " + slots.size());
        }
    }
}
