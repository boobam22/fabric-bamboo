package bamboo.camera.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.client.network.ClientPlayerEntity;

import bamboo.camera.Camera;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void changeLookDirection(double x, double y, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (Camera.isActive() && self instanceof ClientPlayerEntity) {
            Camera.handleMouse(x, y);
            ci.cancel();
        }
    }
}
