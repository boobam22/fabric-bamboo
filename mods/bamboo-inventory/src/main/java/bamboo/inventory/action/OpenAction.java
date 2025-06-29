package bamboo.inventory.action;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;

public class OpenAction {
    public static boolean open(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        ScreenHandlerFactory factory = null;
        if (stack.getItem() == Items.CRAFTING_TABLE) {
            factory = getCraftingTableFactory(cursorStack);
        } else if (stack.getItem() == Items.ENDER_CHEST) {
            factory = getEnderChestFactory(cursorStack);
        } else if (stack.isIn(ItemTags.SHULKER_BOXES)) {
            factory = getShulkerBoxFactory(stack, cursorStack);
        } else {
            return false;
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
            serverPlayer.onHandledScreenClosed();
            serverPlayer.openHandledScreen(new SimpleNamedScreenHandlerFactory(factory, stack.getName()));
        }
        return true;
    }

    private static ScreenHandlerFactory getCraftingTableFactory(ItemStack cursorStack) {
        return (syncId, playerInventory, player) -> {
            ScreenHandlerContext ctx = ScreenHandlerContext.create(player.getWorld(), player.getBlockPos());
            ScreenHandler handler = new InventoryCraftingScreenHandler(syncId, playerInventory, ctx);
            handler.setCursorStack(cursorStack);
            return handler;
        };
    }

    private static ScreenHandlerFactory getEnderChestFactory(ItemStack cursorStack) {
        return (syncId, playerInventory, player) -> {
            EnderChestInventory inventory = player.getEnderChestInventory();
            ScreenHandler handler = GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, inventory);
            handler.setCursorStack(cursorStack);
            return handler;
        };
    }

    private static ScreenHandlerFactory getShulkerBoxFactory(ItemStack stack, ItemStack cursorStack) {
        return (syncId, playerInventory, player) -> {
            SimpleInventory inventory = new SimpleInventory(27);
            stack.get(DataComponentTypes.CONTAINER).copyTo(inventory.getHeldStacks());
            inventory.addListener(si -> {
                ContainerComponent container = ContainerComponent.fromStacks(((SimpleInventory) si).getHeldStacks());
                stack.set(DataComponentTypes.CONTAINER, container);
            });
            ScreenHandler handler = new ShulkerBoxScreenHandler(syncId, playerInventory, inventory);
            handler.setCursorStack(cursorStack);
            return handler;
        };
    }

    private static class InventoryCraftingScreenHandler extends CraftingScreenHandler {
        InventoryCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
            super(syncId, playerInventory, context);
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }
    }
}
