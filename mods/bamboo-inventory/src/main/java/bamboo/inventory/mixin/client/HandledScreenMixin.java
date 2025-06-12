package bamboo.inventory.mixin.client;

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
    protected ScreenHandler handler;

    @Shadow
    protected abstract Slot getSlotAt(double mouseX, double mouseY);

    @Shadow
    protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY,
            CallbackInfoReturnable<Boolean> cir) {
        if (Screen.hasShiftDown() && this.handler.getCursorStack().isEmpty()) {
            Slot slot = this.getSlotAt(mouseX, mouseY);

            if (slot != null && slot.hasStack()) {
                this.onMouseClick(slot, slot.id, button, SlotActionType.QUICK_MOVE);
                cir.setReturnValue(true);
            }
        }
    }
}
