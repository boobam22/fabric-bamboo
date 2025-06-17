package bamboo.inventory.action;

import org.lwjgl.glfw.GLFW;

import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public interface BaseActionInterface extends HandledScreenInterface {
    default void leftClick(Slot slot) {
        clickSlot(slot, GLFW.GLFW_MOUSE_BUTTON_LEFT, SlotActionType.PICKUP);
    }

    default void rightClick(Slot slot) {
        clickSlot(slot, GLFW.GLFW_MOUSE_BUTTON_RIGHT, SlotActionType.PICKUP);
    }

    default void shiftClick(Slot slot) {
        clickSlot(slot, GLFW.GLFW_MOUSE_BUTTON_RIGHT, SlotActionType.QUICK_MOVE);
    }

    default void dropOne(Slot slot) {
        clickSlot(slot, 0, SlotActionType.THROW);
    }

    default void dropAll(Slot slot) {
        clickSlot(slot, 1, SlotActionType.THROW);
    }

    default void leftClickOutside() {
        clickSlot(null, GLFW.GLFW_MOUSE_BUTTON_LEFT, SlotActionType.PICKUP);
    }

    default void rightClickOutside() {
        clickSlot(null, GLFW.GLFW_MOUSE_BUTTON_RIGHT, SlotActionType.PICKUP);
    }
}
