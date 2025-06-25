package bamboo.lib.keybinding.event;

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
}
