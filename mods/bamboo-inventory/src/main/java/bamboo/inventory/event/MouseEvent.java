package bamboo.inventory.event;

import org.lwjgl.glfw.GLFW;

import net.minecraft.screen.slot.Slot;

import bamboo.inventory.action.BaseActionInterface;

public class MouseEvent implements BaseActionInterface {
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

    public boolean handleClick(long window, int button, int action, int modifiers) {
        if (shouldCancel() || action != GLFW.GLFW_PRESS) {
            return false;
        }

        Slot slot = getFocusedSlot();
        if (isKeyPressed(window, GLFW.GLFW_KEY_M)) {
            leftClick(slot);
        } else if (isKeyPressed(window, GLFW.GLFW_KEY_N)) {
            rightClick(slot);
        } else if (isKeyPressed(window, GLFW.GLFW_KEY_B)) {
            shiftClick(slot);
        } else if (isKeyPressed(window, GLFW.GLFW_KEY_V)) {
            dropOne(slot);
        } else if (isKeyPressed(window, GLFW.GLFW_KEY_C)) {
            dropAll(slot);
        } else if (isKeyPressed(window, GLFW.GLFW_KEY_L)) {
            leftClickOutside();
        } else if (isKeyPressed(window, GLFW.GLFW_KEY_K)) {
            rightClickOutside();
        } else {
            return false;
        }

        return true;
    }

    public boolean handleScroll(long window, double horizontal, double vertical) {
        return false;
    }

    public boolean handleMove(long window, double x, double y) {
        return false;
    }
}
