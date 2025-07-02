package bamboo.inventory.command;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class NamedScreenHandlerFactory implements net.minecraft.screen.NamedScreenHandlerFactory {
    private ScreenHandlerFactory factory;
    private Text name;
    private ItemStack cursorStack = ItemStack.EMPTY;

    public NamedScreenHandlerFactory(ScreenHandlerFactory factory, Text name) {
        this.factory = factory;
        this.name = name;
    }

    public void setCursorStack(ItemStack stack) {
        this.cursorStack = stack;
    }

    @Override
    public Text getDisplayName() {
        return this.name;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ScreenHandler handler = this.factory.createMenu(syncId, playerInventory, player);
        handler.setCursorStack(cursorStack);
        return handler;
    }
}
