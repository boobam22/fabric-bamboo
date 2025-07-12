package bamboo.lib.keybinding;

import net.minecraft.client.MinecraftClient;

@FunctionalInterface
public interface Handler {
    boolean apply(MinecraftClient client);
}
