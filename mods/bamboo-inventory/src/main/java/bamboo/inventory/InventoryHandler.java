package bamboo.inventory;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;

import bamboo.lib.keybinding.Handler;
import bamboo.inventory.action.Util;

@FunctionalInterface
public interface InventoryHandler extends Handler {
    @Override
    default boolean apply(MinecraftClient client) {
        if (client.currentScreen instanceof HandledScreen screen) {
            Slot focusedSlot = Util.getFocusedSlot();
            if (focusedSlot != null) {
                return action(screen.getScreenHandler().slots, focusedSlot);
            }
        }
        return false;
    }

    boolean action(List<Slot> slots, Slot focusedSlot);
}
