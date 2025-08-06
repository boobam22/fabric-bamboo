package bamboo.inventory.screen;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.village.VillagerProfession;
import net.minecraft.text.Text;

import bamboo.inventory.mixin.MerchantScreenHandlerAccessor;

public class Util {
    private static void openHandledScreen(ServerPlayerEntity player, ScreenHandlerFactory factory, Text title) {
        player.getServer().execute(() -> {
            NamedScreenHandlerFactory namedScreenHandlerFactory = new NamedScreenHandlerFactory(factory, title);

            ItemStack cursorStack = player.currentScreenHandler.getCursorStack();
            player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
            namedScreenHandlerFactory.setCursorStack(cursorStack);

            player.onHandledScreenClosed();
            player.openHandledScreen(namedScreenHandlerFactory);
        });
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

    public static void openShulkerBox(ServerPlayerEntity player, Slot openedSlot) {
        ItemStack stack = openedSlot.getStack();

        SimpleInventory inventory = new SimpleInventory(27);
        stack.get(DataComponentTypes.CONTAINER).copyTo(inventory.getHeldStacks());
        inventory.addListener(si -> {
            ContainerComponent container = ContainerComponent.fromStacks(((SimpleInventory) si).getHeldStacks());
            stack.set(DataComponentTypes.CONTAINER, container);
        });

        ScreenHandlerFactory factory = (id, pi, p) -> {
            ShulkerBoxScreenHandler handler = new ShulkerBoxScreenHandler(id, pi, inventory);
            if (openedSlot.inventory instanceof PlayerInventory) {
                for (Slot slot : handler.slots) {
                    if (slot.inventory instanceof PlayerInventory && openedSlot.getIndex() == slot.getIndex()) {
                        handler.slots.set(slot.id, new Slot(slot.inventory, slot.getIndex(), 0, 0) {
                            @Override
                            public boolean canTakeItems(PlayerEntity playerEntity) {
                                return false;
                            }
                        });
                        break;
                    }
                }
            }
            return handler;
        };
        openHandledScreen(player, factory, stack.getName());
    }

    public static void refreshTrade(ServerPlayerEntity player) {
        VillagerEntity villager = (VillagerEntity) ((MerchantScreenHandlerAccessor) player.currentScreenHandler)
                .bamboo_getMerchant();
        if (villager.getExperience() == 0
                && !villager.getVillagerData().profession().matchesKey(VillagerProfession.NONE)) {
            villager.setOffers(null);
            player.sendTradeOffers(player.currentScreenHandler.syncId, villager.getOffers(),
                    villager.getVillagerData().level(), villager.getExperience(),
                    villager.isLeveledMerchant(), villager.canRefreshTrades());
        }
    }
}
