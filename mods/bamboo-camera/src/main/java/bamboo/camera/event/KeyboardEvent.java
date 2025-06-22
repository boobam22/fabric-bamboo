package bamboo.camera.event;

import org.lwjgl.glfw.GLFW;

import bamboo.camera.Camera;

public class KeyboardEvent {
    public static final KeyboardEvent INSTANCE = new KeyboardEvent();

    private KeyboardEvent() {
    }

    public boolean handlePress(long window, int key, int scancode, int action, int modifiers) {
        if (key == GLFW.GLFW_KEY_V && action == GLFW.GLFW_PRESS && modifiers == 0) {
            Camera.toggle();
            return true;
        }
        return false;
    }
}
