package bamboo.camera.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;

import bamboo.camera.ClientCamera;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Shadow
    private MinecraftClient client;
    @Shadow
    private Input input;

    @Inject(method = "isCamera", at = @At("HEAD"), cancellable = true)
    private void isCamera(CallbackInfoReturnable<Boolean> cir) {
        if (ClientCamera.isActive()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;tick()V", shift = At.Shift.AFTER))
    private void tickMovement(CallbackInfo ci) {
        if (ClientCamera.isActive()) {
            ClientCamera.handleInput(this.input.playerInput);
            this.input = new KeyboardInput(this.client.options);
        }
    }
}
