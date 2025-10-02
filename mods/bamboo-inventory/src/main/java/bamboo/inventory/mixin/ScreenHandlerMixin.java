package bamboo.inventory.mixin;

import org.lwjgl.glfw.GLFW;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import bamboo.inventory.screen.Util;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    private void onSlotClick(int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (slotId < 0) {
            return;
        }

        ScreenHandler self = (ScreenHandler) (Object) this;
        Slot slot = self.getSlot(slotId);
        ItemStack stack = slot.getStack();

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (stack.isEmpty()) {
                if (slot instanceof TradeOutputSlot) {
                    refreshTrade.apply(player);
                } else {
                    return;
                }
            } else if (stack.getItem() == Items.CRAFTING_TABLE) {
                openCraftingTable.apply(player);
            } else if (stack.getItem() == Items.ENDER_CHEST) {
                openEnderChest.apply(player);
            } else if (stack.isIn(ItemTags.SHULKER_BOXES)) {
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    Util.openShulkerBox(serverPlayer, slot);
                }
            } else {
                return;
            }
            ci.cancel();
        }
    }

    private OnlyServer openCraftingTable = Util::openCraftingTable;
    private OnlyServer openEnderChest = Util::openEnderChest;
    private OnlyServer refreshTrade = bamboo.inventory.villager.Util::refreshTrade;

    private static interface OnlyServer {
        default void apply(PlayerEntity player) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                run(serverPlayer);
            }
        }

        void run(ServerPlayerEntity player);
    }
}
