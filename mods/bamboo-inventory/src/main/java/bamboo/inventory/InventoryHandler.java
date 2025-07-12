package bamboo.inventory;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import bamboo.lib.keybinding.Handler;
import bamboo.inventory.mixin.HandledScreenAccessor;

@FunctionalInterface
public interface InventoryHandler extends Handler {
    @Override
    default boolean apply(MinecraftClient client) {
        if (client.currentScreen instanceof HandledScreen screen) {
            ScreenHandler handler = screen.getScreenHandler();
            Slot focusedSlot = ((HandledScreenAccessor) screen).bamboo_getFocusedSlot();
            if (focusedSlot != null && focusedSlot.hasStack()) {
                action(handler, handler.slots, focusedSlot);
                return true;
            }
        }
        return false;
    }

    void action(ScreenHandler handler, List<Slot> slots, Slot focusedSlot);
}
