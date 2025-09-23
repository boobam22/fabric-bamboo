package bamboo.place.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.MinecraftClient;

import bamboo.place.ClientPlace;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {
    @Inject(method = "onKeyPressed", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void onKeyPressed(Key key, CallbackInfo ci, KeyBinding keyBinding) {
        if (ClientPlace.fastUse.getValue()) {
            if (keyBinding != null && MinecraftClient.getInstance().options.useKey.equals(keyBinding)) {
                ((KeyBindingAccessor) keyBinding).bamboo_setTimesPressed(ClientPlace.usePerTick.getValue());
            }
        }
    }
}
