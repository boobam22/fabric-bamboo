package bamboo.render.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.LightmapTextureManager;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 10)
    private float update(float gamma) {
        return gamma * 16;
    }
}
