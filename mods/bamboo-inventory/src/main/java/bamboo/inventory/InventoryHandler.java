package bamboo.inventory;

import java.util.List;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import bamboo.inventory.mixin.HandledScreenAccessor;

@FunctionalInterface
public interface InventoryHandler extends bamboo.lib.keybinding.handler.ScreenHandler<HandledScreen<?>> {
    @SuppressWarnings("unchecked")
    @Override
    default Class<HandledScreen<?>> screenClass() {
        return (Class<HandledScreen<?>>) (Class<?>) HandledScreen.class;
    }

    @Override
    default void handle(HandledScreen<?> screen) {
        ScreenHandler handler = screen.getScreenHandler();
        Slot focusedSlot = ((HandledScreenAccessor) screen).bamboo_getFocusedSlot();
        action(handler, handler.slots, focusedSlot);
    }

    void action(ScreenHandler handler, List<Slot> slots, Slot focusedSlot);
}
