package bamboo.inventory.event;

import org.lwjgl.glfw.GLFW;

import bamboo.inventory.action.MergeActionInterface;

public class KeyboardEvent implements MergeActionInterface {
    public static final KeyboardEvent INSTANCE = new KeyboardEvent();

    private KeyboardEvent() {
    }

    public boolean handlePress(long window, int key, int scancode, int action, int modifiers) {
        if (!isHandledScreen() || action != GLFW.GLFW_PRESS) {
            return false;
        }

        if (key == GLFW.GLFW_KEY_R && modifiers == 0) {
            merge();
            return true;
        }

        return false;
    }
}
