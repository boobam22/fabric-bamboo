package bamboo.lib.keybinding;

import net.minecraft.client.MinecraftClient;

public class MouseEvent {
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
