package bamboo.render.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import bamboo.render.ClientRender;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(Entity entity, Frustum frustum, double x, double y, double z,
            CallbackInfoReturnable<Boolean> cir) {
        if (ClientRender.disableCorpse.getValue() && !entity.isAlive()
                || ClientRender.disableItem.getValue() && entity.getType() == EntityType.ITEM
                || ClientRender.disableMonster.getValue() && entity.getType().getSpawnGroup() == SpawnGroup.MONSTER) {
            cir.setReturnValue(false);
        }
    }
}
