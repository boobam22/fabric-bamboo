package bamboo.inventory.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Shadow
    private DefaultedList<Slot> slots;

    @Shadow
    protected abstract boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast);

    @Inject(method = "insertItem", at = @At(value = "HEAD"), cancellable = true)
    private void insertItem2(ItemStack stack, int startIndex, int endIndex, boolean fromLast,
            CallbackInfoReturnable<Boolean> cir) {
        if (fromLast && endIndex - startIndex == 36 && this.slots.size() == endIndex) {
            boolean ret = this.insertItem(stack, startIndex, endIndex - 9, false);
            cir.setReturnValue(ret);
        }
    }
}
