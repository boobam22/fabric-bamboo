package bamboo.camera.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.client.network.ClientPlayerEntity;

import bamboo.camera.ClientCamera;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void changeLookDirection(double x, double y, CallbackInfo ci) {
        if (ClientCamera.cameraController.isActive() && (Object) this instanceof ClientPlayerEntity) {
            ClientCamera.cameraController.handleMouse(x, y);
            ci.cancel();
        }
    }
}
