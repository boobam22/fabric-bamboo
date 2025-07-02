package bamboo.lib.keybinding.handler;

import net.minecraft.client.MinecraftClient;

@FunctionalInterface
public interface IngameHandler extends BaseHandler {
    @Override
    default boolean apply(MinecraftClient client) {
        if (client.currentScreen == null) {
            handle(client);
            return true;
        }
        return false;
    }

    void handle(MinecraftClient client);
}
