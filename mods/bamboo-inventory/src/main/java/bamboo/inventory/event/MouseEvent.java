package bamboo.inventory.event;

import org.lwjgl.glfw.GLFW;

import net.minecraft.screen.slot.Slot;

public class MouseEvent implements ClickEventInterface, ScrollEventInterface {
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

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && modifiers == GLFW.GLFW_MOD_SHIFT) {
            return onShiftClick();
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && modifiers == GLFW.GLFW_MOD_SHIFT) {
            return onShiftRightClick();
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && modifiers == GLFW.GLFW_MOD_ALT) {
            return onAltClick();
        } else {
            return false;
        }
    }

    public boolean handleScroll(long window, double horizontal, double vertical) {
        if (shouldCancel()) {
            return false;
        }

        return onScroll();
    }

    public boolean handleMove(long window, double x, double y) {
        if (shouldCancel()) {
            return false;
        }

        if (isKeyPressed(window, GLFW.GLFW_KEY_LEFT_SHIFT)
                && isMousePressed(window, GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            return onShiftClick();
        } else if (isKeyPressed(window, GLFW.GLFW_KEY_LEFT_SHIFT)
                && isMousePressed(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            return onShiftRightClick();
        } else {
            return false;
        }
    }
}
