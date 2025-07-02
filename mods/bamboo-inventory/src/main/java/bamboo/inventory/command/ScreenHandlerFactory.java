package bamboo.inventory.command;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public interface ScreenHandlerFactory extends net.minecraft.screen.ScreenHandlerFactory {
    @Override
    default ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ItemStack cursorStack = player.currentScreenHandler.getCursorStack();

        player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
        ScreenHandler handler = createHandler(syncId, playerInventory);
        handler.setCursorStack(cursorStack);
        return handler;
    }

    ScreenHandler createHandler(int syncId, PlayerInventory playerInventory);
}
