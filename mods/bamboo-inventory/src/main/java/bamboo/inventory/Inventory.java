package bamboo.inventory;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import bamboo.lib.keybinding.Key;
import bamboo.lib.keybinding.Handler;
import bamboo.inventory.action.MoveAction;
import bamboo.inventory.action.MergeAction;

public class Inventory implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Handler cancel = client -> client.currentScreen instanceof HandledScreen;

        Key.parse("shift+left").execute((InventoryHandler) MoveAction::moveOneStack);
        Key.parse("shift+left").triggerOnRelease().execute(cancel);
        Key.parse("shift+left+move").execute((InventoryHandler) MoveAction::moveOneStack);
        Key.parse("shift+right").execute((InventoryHandler) MoveAction::moveOneStackLeaveOne);
        Key.parse("shift+right").triggerOnRelease().execute(cancel);
        Key.parse("shift+right+move").execute((InventoryHandler) MoveAction::moveOneStackLeaveOne);

        Key.parse("ctrl+left").execute((InventoryHandler) MoveAction::moveStacks);
        Key.parse("ctrl+left").triggerOnRelease().execute(cancel);
        Key.parse("ctrl+right").execute((InventoryHandler) MoveAction::moveStacksLeaveOne);
        Key.parse("ctrl+right").triggerOnRelease().execute(cancel);

        Key.parse("alt+left").execute((InventoryHandler) MoveAction::moveAll);
        Key.parse("alt+left").triggerOnRelease().execute(cancel);

        Key.parse("alt+right").execute((InventoryHandler) MoveAction::dropStacks);
        Key.parse("alt+right").triggerOnRelease().execute(cancel);

        Key.parse("scroll").execute((InventoryHandler) MoveAction::craftOne);

        Key.parse("r").execute((InventoryHandler) MergeAction::merge);
    }

    public static void info(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        System.out.println(handler.getClass());
        if (focusedSlot != null) {
            System.out.println(focusedSlot.getClass() + "  " + focusedSlot.id);
            System.out.println(focusedSlot.inventory.getClass() + "  " + slots.size());
        }
    }
}
