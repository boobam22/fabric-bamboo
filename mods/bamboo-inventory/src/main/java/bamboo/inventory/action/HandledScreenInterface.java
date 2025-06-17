package bamboo.inventory.action;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import bamboo.inventory.mixin.HandledScreenAccessor;

public interface HandledScreenInterface {
    private Screen getScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    default boolean isHandledScreen() {
        return getScreen() instanceof HandledScreen;
    }

    default ScreenHandler getHandler() {
        if (isHandledScreen()) {
            return ((HandledScreen<?>) getScreen()).getScreenHandler();
        }
        return null;
    }

    default Slot getFocusedSlot() {
        if (isHandledScreen()) {
            return ((HandledScreenAccessor) (Object) getScreen()).bamboo_getFocusedSlot();
        }
        return null;
    }

    default void clickSlot(Slot slot, int button, SlotActionType type) {
        int id = slot == null ? -999 : slot.id;
        ((HandledScreenAccessor) (Object) getScreen()).bamboo_onMouseClick(slot, id, button, type);
    }
}
