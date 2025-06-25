package bamboo.lib.keybinding.event;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.MinecraftClient;

public class KeyboardEvent {
    private static final HashMap<Integer, ArrayList<Handler>> handlers = new HashMap<>();

    public static void register(int key, Handler handler) {
        handlers.putIfAbsent(key, new ArrayList<>());
        handlers.get(key).add(handler);
    }

    public static boolean handlePress(MinecraftClient client, long window, int key, int action) {
        return false;
    }
}