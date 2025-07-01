package bamboo.pickaxe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

import bamboo.pickaxe.Pickaxe;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow
    private GameMode gameMode;
    @Shadow
    private int blockBreakingCooldown;

    @Inject(method = "breakBlock", at = @At("RETURN"))
    private void breakBlock(CallbackInfoReturnable<Boolean> cir) {
        if (this.gameMode == GameMode.SURVIVAL && Pickaxe.breakCooldown.isAlways() && cir.getReturnValue()) {
            this.blockBreakingCooldown = 5;
        }
    }

    @ModifyConstant(method = "updateBlockBreakingProgress", constant = @Constant(intValue = 5))
    private int updateBlockBreakingProgress(int cooldown) {
        if (this.gameMode == GameMode.SURVIVAL && Pickaxe.breakCooldown.isNever()) {
            return 0;
        }
        return cooldown;
    }

    @Inject(method = "interactBlockInternal", at = @At("HEAD"))
    private void interactBlockInternal(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult,
            CallbackInfoReturnable<ActionResult> cir) {
        if (Pickaxe.areaMine.isEnabled() && player.getStackInHand(hand).isIn(ItemTags.PICKAXES)) {
            Pickaxe.areaMine.resetArea(hitResult.getBlockPos());
        }
    }
}
