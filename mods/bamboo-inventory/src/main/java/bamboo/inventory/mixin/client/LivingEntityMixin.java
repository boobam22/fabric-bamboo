package bamboo.inventory.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ItemEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "createItemEntity", at = @At("RETURN"), cancellable = true)
    private void createItemEntity(CallbackInfoReturnable<ItemEntity> cir) {
        ItemEntity itemEntity = cir.getReturnValue();
        if (itemEntity != null) {
            itemEntity.setPickupDelay(100);
            cir.setReturnValue(itemEntity);
        }
    }
}
