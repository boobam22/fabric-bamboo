package bamboo.camera.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import bamboo.camera.Camera;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @ModifyVariable(method = "updateCrosshairTarget", at = @At("STORE"), ordinal = 0)
    private Entity updateCrosshairTarget(Entity entity) {
        if (Camera.isActive()) {
            return MinecraftClient.getInstance().player;
        }
        return entity;
    }
}
