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
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.GameMode;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import bamboo.pickaxe.ClientPickaxe;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow
    private GameMode gameMode;
    @Shadow
    private int blockBreakingCooldown;

    @Inject(method = "breakBlock", at = @At("RETURN"))
    private void breakBlock(CallbackInfoReturnable<Boolean> cir) {
        if (this.gameMode == GameMode.SURVIVAL && ClientPickaxe.breakCooldown.isAlways() && cir.getReturnValue()) {
            this.blockBreakingCooldown = 5;
        }
    }

    @ModifyConstant(method = "updateBlockBreakingProgress", constant = @Constant(intValue = 5))
    private int updateBlockBreakingProgress(int cooldown) {
        if (this.gameMode == GameMode.SURVIVAL && ClientPickaxe.breakCooldown.isNever()) {
            return 0;
        }
        return cooldown;
    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult,
            CallbackInfoReturnable<ActionResult> cir) {
        if (ClientPickaxe.areaMine.isEnabled() && player.getStackInHand(hand).isIn(ItemTags.PICKAXES)) {
            ClientPickaxe.areaMine.resetArea(hitResult.getBlockPos());
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (ClientPickaxe.areaMine.isEnabled() && player.getMainHandStack().isIn(ItemTags.PICKAXES)) {
            ClientPickaxe.areaMine.expandArea(pos);

            Box box = ClientPickaxe.areaMine.getArea().contract(0.5);
            BlockPos min = BlockPos.ofFloored(box.getMinPos());
            BlockPos max = BlockPos.ofFloored(box.getMaxPos());

            player.networkHandler.sendChatCommand(String.format("bb-pickaxe clean %d %d %d %d %d %d",
                    min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ()));
            cir.setReturnValue(((ClientPlayerInteractionManager) (Object) this).breakBlock(pos));
        }
    }
}
