package bamboo.inventory.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Mouse;
import net.minecraft.client.MinecraftClient;

import bamboo.inventory.event.MouseEvent;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int modifiers, CallbackInfo ci) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle()
                && MouseEvent.INSTANCE.handleClick(window, button, action, modifiers)) {
            ci.cancel();
        }
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle()
                && MouseEvent.INSTANCE.handleScroll(window, horizontal, vertical)) {
            ci.cancel();
        }
    }

    @Inject(method = "onCursorPos", at = @At("TAIL"), cancellable = true)
    private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle()
                && MouseEvent.INSTANCE.handleMove(window, x, y)) {
            ci.cancel();
        }
    }
}
