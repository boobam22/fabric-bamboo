package bamboo.lib.keybinding;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;

public class KeyEvent {
    private static Map<Key, List<Handler>> handlers = new HashMap<>();
    private static Key currentKey = new Key(0, new HashSet<>());

    public static void register(Key key, Handler handler) {
        handlers.putIfAbsent(key, new ArrayList<>());
        handlers.get(key).add(handler);
    }

    public static boolean handlePress(MinecraftClient client, int key, int action) {
        if (action == GLFW.GLFW_REPEAT) {
            return false;
        }

        if (action == GLFW.GLFW_RELEASE) {
            currentKey.modifier.remove(key);
        }

        currentKey.key = key;
        currentKey.action = action;
        boolean cancel = handleCurentKey(client);

        if (action == GLFW.GLFW_PRESS) {
            currentKey.modifier.add(key);
        }
        return cancel;
    }

    public static boolean handleScroll(MinecraftClient client) {
        currentKey.key = KeyMap.SCROLL;
        return handleCurentKey(client);
    }

    public static boolean handleMove(MinecraftClient client) {
        currentKey.key = KeyMap.MOVE;
        return handleCurentKey(client);
    }

    private static boolean handleCurentKey(MinecraftClient client) {
        if (!handlers.containsKey(currentKey)) {
            return false;
        }

        boolean cancel = false;
        for (Handler handler : handlers.get(currentKey)) {
            cancel |= handler.apply(client);
        }
        return cancel;
    }
}
