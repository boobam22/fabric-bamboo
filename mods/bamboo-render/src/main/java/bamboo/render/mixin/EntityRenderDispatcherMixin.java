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

import bamboo.render.Render;
import bamboo.render.ClientRender;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(Entity entity, Frustum frustum, double x, double y, double z,
            CallbackInfoReturnable<Boolean> cir) {
        EntityType<?> type = entity.getType();

        if (ClientRender.disableCorpse.getValue() && !entity.isAlive()
                || ClientRender.disableItem.getValue() && type == EntityType.ITEM
                || ClientRender.disableMonster.getValue() && type.getSpawnGroup() == SpawnGroup.MONSTER
                || type.isIn(Render.DISABLE_RENDER)) {
            cir.setReturnValue(false);
        }
    }
}
