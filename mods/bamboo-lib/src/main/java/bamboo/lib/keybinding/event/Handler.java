package bamboo.lib.keybinding.event;

import java.util.function.Function;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.keybinding.KeyBinding;

public record Handler(KeyBinding keyBinding, Function<MinecraftClient, Boolean> callback) {
}
