package bamboo.inventory.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

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
        if (this.focusedSlot != null && this.focusedSlot.hasStack() && Screen.hasShiftDown()
                && this.handler.getCursorStack().isEmpty()) {
            this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 0, SlotActionType.QUICK_MOVE);
            cir.setReturnValue(true);
        }
    }
}
