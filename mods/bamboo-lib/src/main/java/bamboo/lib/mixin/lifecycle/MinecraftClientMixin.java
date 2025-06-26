package bamboo.lib.mixin.lifecycle;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.lifecycle.MinecraftClientLifecycle;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "joinWorld", at = @At("TAIL"))
    private void joinWorld(CallbackInfo ci) {
        MinecraftClientLifecycle.onJoinWorld();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;integratedServerRunning:Z", opcode = Opcodes.PUTFIELD))
    private void disconnect(CallbackInfo ci) {
        MinecraftClientLifecycle.onExitWorld();
    }

    @Inject(method = "stop", at = @At("HEAD"))
    private void stop(CallbackInfo ci) {
        MinecraftClientLifecycle.onExitGame();
    }
}
