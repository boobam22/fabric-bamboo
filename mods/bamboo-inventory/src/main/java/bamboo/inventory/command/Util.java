package bamboo.inventory.command;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
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
    public static boolean canRefeshTrade(Entity entity) {
        if (entity instanceof VillagerEntity villager) {
            return villager.getExperience() == 0
                    && !villager.getVillagerData().profession().matchesKey(VillagerProfession.NONE);
        }
        return false;
    }

    private static void openHandledScreen(ServerPlayerEntity player, ScreenHandlerFactory factory, Text title) {
        player.onHandledScreenClosed();
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(factory, title));
    }

    public static void openCraftingTable(ServerPlayerEntity player) {
        ScreenHandlerFactory factory = (id, pi) -> new InventoryCraftingScreenHandler(id, player);
        openHandledScreen(player, factory, Items.CRAFTING_TABLE.getName());
    }

    public static void openEnderChest(ServerPlayerEntity player) {
        Inventory inventory = player.getEnderChestInventory();
        ScreenHandlerFactory factory = (id, pi) -> GenericContainerScreenHandler.createGeneric9x3(id, pi, inventory);
        openHandledScreen(player, factory, Items.ENDER_CHEST.getName());
    }

    public static void openShulkerBox(ServerPlayerEntity player, ItemStack stack) {
        SimpleInventory inventory = new SimpleInventory(27);
        stack.get(DataComponentTypes.CONTAINER).copyTo(inventory.getHeldStacks());
        inventory.addListener(si -> {
            ContainerComponent container = ContainerComponent.fromStacks(((SimpleInventory) si).getHeldStacks());
            stack.set(DataComponentTypes.CONTAINER, container);
        });
        ScreenHandlerFactory factory = (id, pi) -> new ShulkerBoxScreenHandler(id, pi, inventory);
        openHandledScreen(player, factory, Items.ENDER_CHEST.getName());
    }

    private static List<Integer> findShulkerBox(ServerPlayerEntity player, Inventory inventory) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isIn(ItemTags.SHULKER_BOXES)) {
                slots.add(i);
            }
        }
        return slots;
    }

    private static List<Integer> findShulkerBox(ServerPlayerEntity player, InventoryType type) {
        if (type == InventoryType.PLAYER_INVENTORY) {
            return findShulkerBox(player, player.getInventory());
        } else if (type == InventoryType.PLAYER_INVENTORY) {
            return findShulkerBox(player, player.getEnderChestInventory());
        }
        return List.of();
    }

    public static List<String> findShulkerBox(ServerPlayerEntity player, String string) {
        return findShulkerBox(player, InventoryType.valueOf(string)).stream().map(String::valueOf).toList();
    }
}
