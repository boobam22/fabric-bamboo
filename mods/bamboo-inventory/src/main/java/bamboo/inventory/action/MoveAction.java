package bamboo.inventory.action;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.CrafterScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

public class MoveAction {
    public static void moveOneStack(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        if (focusedSlot == null || !focusedSlot.hasStack()) {
            return;
        }

        if (focusedSlot.inventory instanceof PlayerInventory) {
            Util.quickMove(focusedSlot);
            return;
        }

        List<Slot> availableSlots = findAvailableSlots(handler, slots, focusedSlot, 0);
        if (handleCursorStack(handler, slots, availableSlots)) {
            Util.leftClick(focusedSlot);
            availableSlots.forEach(slot -> {
                Util.leftClick(slot);
            });
        }
    }

    public static void moveOneStackLeaveOne(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        if (!isContainerScreen(handler) || focusedSlot == null || focusedSlot.getStack().getCount() <= 1) {
            return;
        }

        List<Slot> availableSlots = findAvailableSlots(handler, slots, focusedSlot, -1);
        if (handleCursorStack(handler, slots, availableSlots)) {
            Util.leftClick(focusedSlot);
            Util.rightClick(focusedSlot);
            availableSlots.forEach(slot -> {
                Util.leftClick(slot);
            });
        }
    }

    public static void moveStacks(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        if (!isContainerScreen(handler) || focusedSlot == null || !focusedSlot.hasStack()) {
            return;
        }

        ItemStack focusedStack = focusedSlot.getStack().copy();
        for (Slot slot : findSelfInventory(handler, slots, focusedSlot)) {
            if (ItemStack.areItemsAndComponentsEqual(focusedStack, slot.getStack())) {
                moveOneStack(handler, slots, slot);
            }
        }
    }

    public static void moveStacksLeaveOne(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        if (!isContainerScreen(handler) || focusedSlot == null || !focusedSlot.hasStack()) {
            return;
        }

        ItemStack focusedStack = focusedSlot.getStack().copy();
        for (Slot slot : findSelfInventory(handler, slots, focusedSlot)) {
            if (ItemStack.areItemsAndComponentsEqual(focusedStack, slot.getStack())) {
                moveOneStackLeaveOne(handler, slots, slot);
            }
        }
    }

    public static void moveAll(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        if (!isContainerScreen(handler) || focusedSlot == null) {
            return;
        }

        for (Slot slot : findSelfInventory(handler, slots, focusedSlot)) {
            if (slot.hasStack()) {
                moveOneStack(handler, slots, slot);
            }
        }
    }

    public static void dropStacks(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        if (focusedSlot == null || !focusedSlot.hasStack()) {
            return;
        }

        Slot cursor = null;
        ItemStack stack = handler.getCursorStack();
        if (!stack.isEmpty()) {
            for (Slot slot : slots) {
                if (!slot.hasStack() && slot.canInsert(stack)) {
                    Util.leftClick(slot);
                    cursor = slot;
                    break;
                }
            }
        }

        ItemStack focusedStack = focusedSlot.getStack().copy();
        for (Slot slot : findSelfInventory(handler, slots, focusedSlot)) {
            if (slot == cursor) {
                continue;
            }
            if (ItemStack.areItemsAndComponentsEqual(focusedStack, slot.getStack())) {
                Util.dropOneStack(slot);
            }
        }

        if (cursor != null) {
            Util.leftClick(cursor);
        }
    }

    public static void craftOrBuyOne(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        if (focusedSlot instanceof CraftingResultSlot || focusedSlot instanceof TradeOutputSlot) {
            List<Slot> availableSlots = findAvailableSlots(handler, slots, focusedSlot, 0);
            if (handleCursorStack(handler, slots, availableSlots)) {
                Util.leftClick(focusedSlot);
                availableSlots.forEach(slot -> {
                    Util.leftClick(slot);
                });
            }
        }
    }

    public static void buyAll(MerchantScreenHandler handler, Runnable select) {
        Slot output = handler.getSlot(2);

        while (true) {
            select.run();
            if (!output.hasStack()) {
                break;
            }

            while (output.hasStack()) {
                MoveAction.craftOrBuyOne(handler, handler.slots, output);
            }
        }
    }

    private static boolean isContainerScreen(ScreenHandler handler) {
        return Util.isChestScreen(handler)
                || handler instanceof Generic3x3ContainerScreenHandler
                || handler instanceof HopperScreenHandler;
    }

    private static List<Slot> findInventory(ScreenHandler handler, List<Slot> slots, Slot focusedSlot, boolean self) {
        if (focusedSlot == null) {
        } else if (focusedSlot.inventory instanceof PlayerInventory ^ self) {
            if (handler instanceof PlayerScreenHandler) {
                if (focusedSlot.id >= 9 && focusedSlot.id < 36) {
                    return slots.subList(36, 45);
                } else {
                    return slots.subList(9, 36);
                }
            } else if (isContainerScreen(handler)) {
                return slots.subList(0, slots.size() - 36);
            } else if (handler instanceof CrafterScreenHandler) {
                return slots.subList(0, 9);
            }
        } else {
            return Util.findPlayerInventory(slots);
        }

        return List.of();
    }

    private static List<Slot> findSelfInventory(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        return findInventory(handler, slots, focusedSlot, true);
    }

    private static List<Slot> findTargetInventory(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        return findInventory(handler, slots, focusedSlot, false);
    }

    private static List<Slot> findAvailableSlots(List<Slot> inventory, Slot focusedSlot, int n) {
        List<Slot> availableSlots = new ArrayList<>();

        if (focusedSlot == null || !focusedSlot.hasStack()) {
            return availableSlots;
        }

        if (n <= 0) {
            n += focusedSlot.getStack().getCount();
            if (n <= 0) {
                return availableSlots;
            }
        }

        ItemStack focusedStack = focusedSlot.getStack();
        Slot empty = null;
        int remain = n;

        for (Slot slot : inventory) {
            ItemStack stack = slot.getStack();
            if (stack.isEmpty() && slot.canInsert(focusedStack)) {
                if (empty == null) {
                    empty = slot;
                    if (!focusedStack.isStackable()) {
                        break;
                    }
                } else {
                    continue;
                }
            }

            if (!stack.isStackable()) {
                continue;
            }

            if (ItemStack.areItemsAndComponentsEqual(stack, focusedStack)) {
                int size = stack.getMaxCount() - stack.getCount();
                if (size > 0) {
                    remain -= size;
                    availableSlots.add(slot);
                }
                if (remain <= 0) {
                    break;
                }
            }
        }

        if (remain > 0) {
            if (empty != null) {
                availableSlots.add(empty);
            } else if (remain < n) {
                availableSlots.add(focusedSlot);
            }
        }

        return availableSlots;
    }

    private static List<Slot> findAvailableSlots(ScreenHandler handler, List<Slot> slots, Slot focusedSlot, int n) {
        List<Slot> inventory = findTargetInventory(handler, slots, focusedSlot);
        return findAvailableSlots(inventory, focusedSlot, n);
    }

    private static boolean handleCursorStack(ScreenHandler handler, List<Slot> inventory, List<Slot> slots) {
        if (slots.size() == 0 || slots.getLast() instanceof CraftingResultSlot) {
            return false;
        }

        ItemStack stack = handler.getCursorStack();
        if (stack.isEmpty()) {
            return true;
        }

        for (Slot slot : inventory) {
            if (slot.hasStack() || !slot.canInsert(stack)
                    || slot.inventory instanceof CraftingInventory || slots.contains(slot)) {
                continue;
            }
            Util.leftClick(slot);
            slots.add(slot);
            return true;
        }
        return false;
    }
}
