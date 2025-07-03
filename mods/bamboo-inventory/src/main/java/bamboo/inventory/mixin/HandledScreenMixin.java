package bamboo.inventory.mixin;

import org.lwjgl.glfw.GLFW;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"), cancellable = true)
    private void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            return;
        }

        ItemStack stack = slot.getStack();
        ItemStack cursorStack = ((HandledScreen<?>) (Object) this).getScreenHandler().getCursorStack();
        if (slot instanceof TradeOutputSlot && stack.isEmpty() && cursorStack.isEmpty()) {
            sendChatCommand("bb-inventory refresh-trade");
        } else if (stack.getItem() == Items.CRAFTING_TABLE) {
            sendChatCommand("bb-inventory open-crafting-table");
        } else if (stack.getItem() == Items.ENDER_CHEST) {
            sendChatCommand("bb-inventory open-ender-chest");
        } else if (stack.isIn(ItemTags.SHULKER_BOXES)) {
            sendChatCommand("bb-inventory open-shulker-box " + slotId);
        } else {
            return;
        }
        ci.cancel();
    }

    private void sendChatCommand(String cmd) {
        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand(cmd);
    }
}
