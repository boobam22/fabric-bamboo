package bamboo.inventory.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow
    private ScreenHandler handler;
    @Shadow
    private Slot focusedSlot;

    @Shadow
    protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void mouseDragged(CallbackInfoReturnable<Boolean> cir) {
        if (this.focusedSlot == null
                || !this.focusedSlot.hasStack()
                || !Screen.hasShiftDown()
                || !this.handler.getCursorStack().isEmpty()) {
            return;
        }

        this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 0, SlotActionType.QUICK_MOVE);
        cir.setReturnValue(true);
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"), cancellable = true)
    private void onMouseClick2(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (actionType != SlotActionType.QUICK_MOVE
                || slot == null
                || !slot.hasStack()
                || slot.inventory.getClass() == PlayerInventory.class) {
            return;
        }

        this.onMouseClick(slot, slot.id, 0, SlotActionType.PICKUP);
        ItemStack cursorStack = this.handler.getCursorStack();

        int count = 0;
        for (Slot tmp : this.handler.slots) {
            if (tmp.inventory.getClass() != PlayerInventory.class) {
                continue;
            }

            ItemStack stack = tmp.getStack();
            if (stack.isEmpty()) {
                this.onMouseClick(tmp, tmp.id, 0, SlotActionType.PICKUP);
                break;
            } else if (ItemStack.areItemsEqual(cursorStack, stack) && (stack.getMaxCount() > stack.getCount())) {
                this.onMouseClick(tmp, tmp.id, 0, SlotActionType.PICKUP);
                cursorStack = this.handler.getCursorStack();
                if (cursorStack.isEmpty()) {
                    break;
                }
            }

            count += 1;
            if (count == 27) {
                if (!cursorStack.isEmpty()) {
                    this.onMouseClick(slot, slot.id, 0, SlotActionType.PICKUP);
                }
                break;
            }
        }

        ci.cancel();
    }
}
