package bamboo.place.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.dimension.NetherPortal;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

@Mixin(NetherPortal.class)
public abstract class NetherPortalMixin {
    @Inject(method = "validStateInsidePortal", at = @At("RETURN"), cancellable = true)
    private static void validStateInsidePortal(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || state.isOf(Blocks.WATER));
    }
}
