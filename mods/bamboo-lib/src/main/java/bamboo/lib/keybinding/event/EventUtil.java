package bamboo.lib.keybinding.event;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import bamboo.lib.keybinding.KeyBinding;
import bamboo.lib.keybinding.KeyMap;
import bamboo.lib.keybinding.Handler;

public class EventUtil {
    public static void register(KeyBinding keyBinding, Handler handler) {
        if (KeyMap.isMouseKey(keyBinding.key)) {
            MouseEvent.register(keyBinding, handler);
        } else {
            KeyboardEvent.register(keyBinding, handler);
        }
    }

    private static boolean isPressed(long window, int key) {
        if (KeyMap.isMouseKey(key)) {
            return GLFW.glfwGetMouseButton(window, key) != GLFW.GLFW_RELEASE;
        } else {
            return GLFW.glfwGetKey(window, key) != GLFW.GLFW_RELEASE;
        }
    }

    public static boolean matchModifier(long window, List<Integer> modifier) {
        for (int key : modifier) {
            if (!isPressed(window, key)) {
                return false;
            }
        }
        return true;
    }
}
