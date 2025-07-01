package bamboo.pickaxe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.WorldAccess;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import bamboo.pickaxe.Pickaxe;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "onBroken", at = @At("HEAD"))
    private void onBroken(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!world.isClient()) {
            Pickaxe.areaMine.mine((ServerWorld) world, pos);
        }
    }
}
