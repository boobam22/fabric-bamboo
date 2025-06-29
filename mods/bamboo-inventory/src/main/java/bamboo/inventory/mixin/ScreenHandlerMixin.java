package bamboo.inventory.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;

import bamboo.inventory.action.OpenAction;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Inject(method = "handleSlotClick", at = @At("HEAD"), cancellable = true)
    private void handleSlotClick(PlayerEntity player, ClickType clickType, Slot slot, ItemStack stack,
            ItemStack cursorStack, CallbackInfoReturnable<Boolean> cir) {
        if (clickType == ClickType.RIGHT && OpenAction.open(player, stack, cursorStack)) {
            cir.setReturnValue(true);
        }
    }
}
