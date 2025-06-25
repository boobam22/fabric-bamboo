package bamboo.lib.keybinding.event;

import java.util.function.Function;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.keybinding.KeyBinding;
import bamboo.lib.keybinding.KeyMap;

public class EventUtil {
    public static void register(KeyBinding keyBinding, Function<MinecraftClient, Boolean> callback) {
        int key = keyBinding.key;
        Handler handler = new Handler(keyBinding, callback);

        if (KeyMap.isMouseKey(key)) {
            MouseEvent.register(key, handler);
        } else {
            KeyboardEvent.register(key, handler);
        }
    }
}
