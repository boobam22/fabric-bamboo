package bamboo.lib.keybinding;

import net.minecraft.client.MinecraftClient;

public class KeyEvent {
    public static boolean handlePress(MinecraftClient client, int key, int action) {
        return false;
    }

    public static boolean handleScroll(MinecraftClient client) {
        return false;
    }

    public static boolean handleMove(MinecraftClient client) {
        return false;
    }
}
