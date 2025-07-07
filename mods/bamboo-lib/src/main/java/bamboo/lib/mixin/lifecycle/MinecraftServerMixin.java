package bamboo.lib.mixin.lifecycle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import bamboo.lib.Lib;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "loadWorld", at = @At("TAIL"))
    private void loadWorld(CallbackInfo ci) {
        Lib.startHandlers.runAll();
    }

    @Inject(method = "shutdown", at = @At("TAIL"))
    private void shutdown(CallbackInfo ci) {
        Lib.shutdownHandlers.runAll();
    }
}
