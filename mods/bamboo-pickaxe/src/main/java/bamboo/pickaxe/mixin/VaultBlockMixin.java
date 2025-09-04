package bamboo.pickaxe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.VaultBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.enums.VaultState;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(VaultBlock.class)
public abstract class VaultBlockMixin {
    @Inject(method = "onUseWithItem", at = @At("HEAD"), cancellable = true)
    private void onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world instanceof ServerWorld serverWorld
                && state.get(Properties.VAULT_STATE) == VaultState.INACTIVE
                && stack.isIn(ItemTags.PICKAXES)) {
            VaultBlockEntity vaultBlockEntity = (VaultBlockEntity) serverWorld.getBlockEntity(pos);
            ((VaultServerDataAccessor) vaultBlockEntity.getServerData()).bamboo_getRewardedPlayers().clear();
            cir.setReturnValue(ActionResult.SUCCESS_SERVER);
        }
    }
}