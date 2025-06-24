package bamboo.lib.mixin.keybinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;

import bamboo.lib.keybinding.KeyboardEvent;;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == client.getWindow().getHandle()
                && KeyboardEvent.handlePress(client, window, key, action)) {
            ci.cancel();
        }
    }
}
