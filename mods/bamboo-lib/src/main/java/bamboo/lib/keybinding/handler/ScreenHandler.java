package bamboo.lib.keybinding.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public interface ScreenHandler<T extends Screen> extends BaseHandler {
    @Override
    default boolean apply(MinecraftClient client) {
        Screen screen = client.currentScreen;
        if (screenClass().isInstance(screen)) {
            handle(screenClass().cast(screen));
            return true;
        }
        return false;
    }

    Class<T> screenClass();

    void handle(T screen);
}
