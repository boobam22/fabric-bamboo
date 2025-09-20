package bamboo.pickaxe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.tag.ItemTags;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    private void dropLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (causedByPlayer && !(self instanceof EnderDragonEntity) && !(self instanceof PlayerEntity)) {
            PlayerEntity player = self.getAttackingPlayer();
            if (player != null && player.getMainHandStack().isIn(ItemTags.PICKAXES)) {
                self.dropStack(world, self.getPickBlockStack());
                ci.cancel();
            }
        }
    }
}
