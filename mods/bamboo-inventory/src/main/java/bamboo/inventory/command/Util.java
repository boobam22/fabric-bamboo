package bamboo.inventory.command;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;

public class Util {
    public static boolean isShulkerBox(ItemStack stack) {
        return stack != null && stack.isIn(ItemTags.SHULKER_BOXES);
    }

    private static void openHandledScreen(ServerPlayerEntity player, ScreenHandlerFactory factory, Text title) {
        NamedScreenHandlerFactory namedScreenHandlerFactory = new NamedScreenHandlerFactory(factory, title);

        ItemStack cursorStack = player.currentScreenHandler.getCursorStack();
        player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
        namedScreenHandlerFactory.setCursorStack(cursorStack);

        player.onHandledScreenClosed();
        player.openHandledScreen(namedScreenHandlerFactory);
    }

    public static void openCraftingTable(ServerPlayerEntity player) {
        ScreenHandlerFactory factory = (id, pi, p) -> new CraftingScreenHandler(id, pi, player);
        openHandledScreen(player, factory, Items.CRAFTING_TABLE.getName());
    }

    public static void openEnderChest(ServerPlayerEntity player) {
        Inventory inventory = player.getEnderChestInventory();
        ScreenHandlerFactory factory = (id, pi, p) -> GenericContainerScreenHandler.createGeneric9x3(id, pi, inventory);
        openHandledScreen(player, factory, Items.ENDER_CHEST.getName());
    }

    public static void openShulkerBox(ServerPlayerEntity player, ItemStack stack) {
        if (!isShulkerBox(stack)) {
            return;
        }

        SimpleInventory inventory = new SimpleInventory(27);
        stack.get(DataComponentTypes.CONTAINER).copyTo(inventory.getHeldStacks());
        inventory.addListener(si -> {
            ContainerComponent container = ContainerComponent.fromStacks(((SimpleInventory) si).getHeldStacks());
            stack.set(DataComponentTypes.CONTAINER, container);
        });
        ScreenHandlerFactory factory = (id, pi, p) -> new ShulkerBoxScreenHandler(id, pi, inventory);
        openHandledScreen(player, factory, stack.getName());
    }

    public static boolean canRefeshTrade(Entity entity) {
        if (entity instanceof VillagerEntity villager) {
            return villager.getExperience() == 0
                    && !villager.getVillagerData().profession().matchesKey(VillagerProfession.NONE);
        }
        return false;
    }

    public static void refreshTrade(ServerPlayerEntity player, Entity entity) {
        if (canRefeshTrade(entity)) {
            VillagerEntity villager = (VillagerEntity) entity;
            villager.setOffers(null);

            if (player.currentScreenHandler instanceof MerchantScreenHandler) {
                player.sendTradeOffers(player.currentScreenHandler.syncId, villager.getOffers(),
                        villager.getVillagerData().level(), villager.getExperience(),
                        villager.isLeveledMerchant(), villager.canRefreshTrades());
            }
        }
    }
}
