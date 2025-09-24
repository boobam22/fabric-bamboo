package bamboo.inventory.mixin;

import org.lwjgl.glfw.GLFW;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import bamboo.inventory.ClientInventory;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    private ClientPlayerEntity player;
    @Shadow
    private ClientPlayerInteractionManager interactionManager;

    @Inject(method = "doItemUse", at = @At("HEAD"))
    private void doItemUse(CallbackInfo ci) {
        if (!ClientInventory.handRestock.getValue() || player.isUsingItem() || player.isInCreativeMode()) {
            return;
        }

        for (Hand hand : Hand.values()) {
            ItemStack handStack = player.getStackInHand(hand);
            if (handStack.getMaxCount() == 1) {
                continue;
            }

            int half = handStack.getMaxCount() / 2;
            if (!handStack.isEmpty() && handStack.getCount() <= half) {
                for (int i = 35; i >= 9; i--) {
                    ItemStack stack = player.playerScreenHandler.getSlot(i).getStack();
                    if (ItemStack.areItemsAndComponentsEqual(handStack, stack)) {
                        if (stack.getCount() <= half) {
                            leftClick(i);
                        } else {
                            rightClick(i);
                        }

                        int slotId = hand == Hand.MAIN_HAND ? player.getInventory().getSelectedSlot() + 36 : 45;
                        leftClick(slotId);
                        break;
                    }
                }
            }
        }
    }

    private void leftClick(int slotId) {
        interactionManager.clickSlot(player.playerScreenHandler.syncId,
                slotId, GLFW.GLFW_MOUSE_BUTTON_LEFT, SlotActionType.PICKUP, player);
    }

    private void rightClick(int slotId) {
        interactionManager.clickSlot(player.playerScreenHandler.syncId,
                slotId, GLFW.GLFW_MOUSE_BUTTON_RIGHT, SlotActionType.PICKUP, player);
    }
}
