package bamboo.lib.keybinding.handler;

import net.minecraft.client.MinecraftClient;

@FunctionalInterface
public interface BaseHandler {
    boolean apply(MinecraftClient client);
}
