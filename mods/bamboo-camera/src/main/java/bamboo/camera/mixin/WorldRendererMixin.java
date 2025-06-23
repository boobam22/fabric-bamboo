package bamboo.camera.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Inject(method = "getEntitiesToRender", at = @At("TAIL"))
    private void getEntitiesToRender(Camera camera, Frustum frustum, List<Entity> output,
            CallbackInfoReturnable<Boolean> cir) {
        if (bamboo.camera.Camera.isActive()) {
            output.add(MinecraftClient.getInstance().player);
        }
    }
}
