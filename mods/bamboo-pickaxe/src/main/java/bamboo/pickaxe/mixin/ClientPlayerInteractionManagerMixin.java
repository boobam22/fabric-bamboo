package bamboo.pickaxe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.world.GameMode;

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
}
