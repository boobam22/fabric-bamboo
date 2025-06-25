package bamboo.lib.keybinding.event;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.keybinding.KeyMap;

public class MouseEvent {
    private static final HashMap<Integer, ArrayList<Handler>> clickHandlers = new HashMap<>();
    private static final ArrayList<Handler> scrollHandlers = new ArrayList<>();
    private static final ArrayList<Handler> moveHandlers = new ArrayList<>();

    public static void register(int key, Handler handler) {
        if (key == KeyMap.SCROLL) {
            scrollHandlers.add(handler);
        } else if (key == KeyMap.MOVE) {
            moveHandlers.add(handler);
        } else {
            clickHandlers.putIfAbsent(key, new ArrayList<>());
            clickHandlers.get(key).add(handler);
        }
    }

    public static boolean handleClick(MinecraftClient client, long window, int button, int action) {
        return false;
    }

    public static boolean handleScroll(MinecraftClient client, long window, double horizontal, double vertical) {
        return false;
    }

    public static boolean handleMove(MinecraftClient client, long window, double x, double y) {
        return false;
    }
}
