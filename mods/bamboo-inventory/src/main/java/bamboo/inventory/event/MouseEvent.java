package bamboo.inventory.event;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerInventory;

import bamboo.inventory.action.MoveActionInterface;
import bamboo.inventory.action.FindSlotInterface;

public class MouseEvent implements MoveActionInterface, FindSlotInterface {
    public static final MouseEvent INSTANCE = new MouseEvent();

    private MouseEvent() {
    }

    private boolean shouldCancel() {
        Slot slot = getFocusedSlot();
        return slot == null || !slot.hasStack();
    }

    private boolean isKeyPressed(long window, int key) {
        return GLFW.glfwGetKey(window, key) != GLFW.GLFW_RELEASE;
    }

    private boolean isMousePressed(long window, int button) {
        return GLFW.glfwGetMouseButton(window, button) != GLFW.GLFW_RELEASE;
    }

    public boolean handleClick(long window, int button, int action, int modifiers) {
        if (shouldCancel() || action != GLFW.GLFW_PRESS) {
            return false;
        }

        Slot slot = getFocusedSlot();
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && modifiers == GLFW.GLFW_MOD_SHIFT) {
            return onShiftClick(slot);
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && modifiers == GLFW.GLFW_MOD_SHIFT) {
            return onShiftRightClick(slot);
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && modifiers == GLFW.GLFW_MOD_ALT) {
            return onAltClick(slot);
        } else {
            return false;
        }
    }

    public boolean handleScroll(long window, double horizontal, double vertical) {
        return false;
    }

    public boolean handleMove(long window, double x, double y) {
        return false;
    }

    private boolean onShiftClick(Slot from) {
        if (from.inventory instanceof PlayerInventory) {
            shiftClick(from);
            return true;
        }

        List<Slot> to = findTargetSlots(from, from.getStack().getCount());
        if (to != null) {
            moveStack(from, to);
            return true;
        }
        return false;
    }

    private boolean onShiftRightClick(Slot from) {
        List<Slot> to = findTargetSlots(from, from.getStack().getCount() - 1);
        if (to != null) {
            moveLeaveOne(from, to);
            return true;
        }
        return false;
    }

    private boolean onAltClick(Slot focusedSlot) {
        List<Slot> to = findTargetSlots(focusedSlot, focusedSlot.getStack().getCount());
        if (to == null) {
            return false;
        }

        for (Slot slot : getHandler().slots) {
            if (slot != focusedSlot
                    && slot.inventory == focusedSlot.inventory
                    && slot.hasStack()
                    && ItemStack.areItemsAndComponentsEqual(slot.getStack(), focusedSlot.getStack())) {
                onShiftClick(slot);
            }
        }
        onShiftClick(focusedSlot);
        return true;
    }
}
