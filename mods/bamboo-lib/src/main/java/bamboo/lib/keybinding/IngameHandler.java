package bamboo.lib.keybinding;

import net.minecraft.client.MinecraftClient;

@FunctionalInterface
public interface IngameHandler extends Handler {
    @Override
    default boolean apply(MinecraftClient client) {
        if (client.currentScreen == null) {
            return handle(client);
        }
        return false;
    }

    boolean handle(MinecraftClient client);
}
