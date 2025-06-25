package bamboo.lib.keybinding.event;

import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.keybinding.KeyBinding;
import bamboo.lib.keybinding.Handler;

public class KeyboardEvent {
    private static final HashMap<Integer, LinkedHashMap<KeyBinding, Handler>> handlers = new HashMap<>();

    public static void register(KeyBinding keyBinding, Handler handler) {
        int key = keyBinding.key;
        handlers.putIfAbsent(key, new LinkedHashMap<>());
        handlers.get(key).putIfAbsent(keyBinding, handler);
    }

    public static boolean handlePress(MinecraftClient client, long window, int key, int action) {
        return false;
    }
}