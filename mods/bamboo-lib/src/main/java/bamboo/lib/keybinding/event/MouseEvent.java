package bamboo.lib.keybinding.event;

import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.keybinding.KeyBinding;
import bamboo.lib.keybinding.Handler;
import bamboo.lib.keybinding.KeyMap;

public class MouseEvent {
    private static final HashMap<Integer, LinkedHashMap<KeyBinding, Handler>> clickHandlers = new HashMap<>();
    private static final LinkedHashMap<KeyBinding, Handler> scrollHandlers = new LinkedHashMap<>();
    private static final LinkedHashMap<KeyBinding, Handler> moveHandlers = new LinkedHashMap<>();

    public static void register(KeyBinding keyBinding, Handler handler) {
        int key = keyBinding.key;

        if (key == KeyMap.SCROLL) {
            scrollHandlers.putIfAbsent(keyBinding, handler);
        } else if (key == KeyMap.MOVE) {
            moveHandlers.putIfAbsent(keyBinding, handler);
        } else {
            clickHandlers.putIfAbsent(key, new LinkedHashMap<>());
            clickHandlers.get(key).putIfAbsent(keyBinding, handler);
        }
    }

    public static boolean handleClick(MinecraftClient client, long window, int button, int action) {
        if (!clickHandlers.containsKey(button)) {
            return false;
        }

        for (KeyBinding keyBinding : clickHandlers.get(button).keySet()) {
            if (keyBinding.action == action && EventUtil.matchModifier(window, keyBinding.modifier)) {
                return clickHandlers.get(button).get(keyBinding).apply(client);
            }
        }
        return false;
    }

    public static boolean handleScroll(MinecraftClient client, long window, double horizontal, double vertical) {
        for (KeyBinding keyBinding : scrollHandlers.keySet()) {
            if (EventUtil.matchModifier(window, keyBinding.modifier)) {
                return scrollHandlers.get(keyBinding).apply(client);
            }
        }
        return false;
    }

    public static boolean handleMove(MinecraftClient client, long window, double x, double y) {
        for (KeyBinding keyBinding : moveHandlers.keySet()) {
            if (EventUtil.matchModifier(window, keyBinding.modifier)) {
                return moveHandlers.get(keyBinding).apply(client);
            }
        }
        return false;
    }
}
