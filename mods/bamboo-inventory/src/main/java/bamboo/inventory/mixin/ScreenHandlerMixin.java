package bamboo.inventory.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ClickType;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Inject(method = "handleSlotClick", at = @At("HEAD"), cancellable = true)
    private void handleSlotClick(PlayerEntity player, ClickType clickType, Slot slot, ItemStack stack,
            ItemStack cursorStack, CallbackInfoReturnable<Boolean> cir) {
        if (clickType == ClickType.LEFT) {
            return;
        }

        if (stack.isIn(ItemTags.SHULKER_BOXES)) {
        } else if (stack.getItem() == Items.ENDER_CHEST) {
            Inventory inventory = player.getEnderChestInventory();
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, serverPlayer) -> {
                return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, inventory);
            }, stack.getName()));
            cir.setReturnValue(true);
        }
    }
}
