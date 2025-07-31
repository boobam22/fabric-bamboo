package bamboo.inventory.action;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.screen.slot.Slot;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

public class MoveAction {
    private static List<Slot> findInventory(List<Slot> slots, Slot focusedSlot, boolean self) {
        if (!Util.isContainerScreen()) {
            return List.of();
        }

        int offset = slots.size() - 36;
        if (focusedSlot.id < offset ^ self) {
            return slots.subList(offset, offset + 27).reversed();
        } else {
            return slots.subList(0, offset);
        }
    }

    private static List<Slot> findSelfInventory(List<Slot> slots, Slot focusedSlot) {
        return findInventory(slots, focusedSlot, true);
    }

    private static List<Slot> findTargetInventory(List<Slot> slots, Slot focusedSlot) {
        return findInventory(slots, focusedSlot, false);
    }

    private static List<Slot> findPlayerInventory(List<Slot> slots) {
        if (Util.isInventoryScreen()) {
            return slots.subList(9, 36);
        } else if (Util.isContainerScreen() || Util.isCraftingScreen() || Util.isMerchantScreen()) {
            return slots.subList(slots.size() - 36, slots.size() - 9);
        } else {
            return List.of();
        }
    }

    private static List<Slot> findAvailableSlots(List<Slot> inventory, Slot focusedSlot, int n) {
        List<Slot> availableSlots = new ArrayList<>();

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

    private static boolean handleCursorStack(List<Slot> inventory, List<Slot> slots) {
        if (slots.size() == 0 || Util.isCraftingResultSlot(slots.getLast())) {
            return false;
        }

        ItemStack stack = Util.getCursorStack();
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

    private static void clickSlots(List<Slot> inventory, Slot focusedSlot, boolean leaveOne) {
        List<Slot> availableSlots = findAvailableSlots(inventory, focusedSlot, leaveOne ? -1 : 0);
        if (handleCursorStack(inventory, availableSlots)) {
            Util.leftClick(focusedSlot);
            if (leaveOne) {
                Util.rightClick(focusedSlot);
            }

            availableSlots.forEach(slot -> {
                Util.leftClick(slot);
            });
        }
    }

    private static void clickSlots(List<Slot> inventory, Slot focusedSlot) {
        clickSlots(inventory, focusedSlot, false);
    }

    public static boolean moveOneStack(List<Slot> slots, Slot focusedSlot) {
        if (Util.isCraftingResultSlot(focusedSlot) || Util.isInventoryScreen()) {
            Util.quickMove(focusedSlot);
        } else if (Util.isContainerScreen()) {
            List<Slot> inventory = findTargetInventory(slots, focusedSlot);
            clickSlots(inventory, focusedSlot);
        } else {
            return false;
        }
        return true;
    }

    public static boolean moveOneStackLeaveOne(List<Slot> slots, Slot focusedSlot) {
        if (Util.isContainerScreen()) {
            List<Slot> inventory = findTargetInventory(slots, focusedSlot);
            clickSlots(inventory, focusedSlot, true);
        } else {
            return false;
        }
        return true;
    }

    public static boolean moveStacks(List<Slot> slots, Slot focusedSlot) {
        ItemStack focusedStack = focusedSlot.getStack().copy();
        for (Slot slot : findSelfInventory(slots, focusedSlot)) {
            if (ItemStack.areItemsAndComponentsEqual(focusedStack, slot.getStack())) {
                moveOneStack(slots, slot);
            }
        }
        return true;
    }

    public static boolean moveStacksLeaveOne(List<Slot> slots, Slot focusedSlot) {
        ItemStack focusedStack = focusedSlot.getStack().copy();
        for (Slot slot : findSelfInventory(slots, focusedSlot)) {
            if (ItemStack.areItemsAndComponentsEqual(focusedStack, slot.getStack())) {
                moveOneStackLeaveOne(slots, slot);
            }
        }
        return true;
    }

    public static boolean moveAll(List<Slot> slots, Slot focusedSlot) {
        for (Slot slot : findSelfInventory(slots, focusedSlot)) {
            moveOneStack(slots, slot);
        }
        return true;
    }

    public static boolean dropStacks(List<Slot> slots, Slot focusedSlot) {
        if (!Util.getCursorStack().isEmpty()) {
            return false;
        }

        ItemStack focusedStack = focusedSlot.getStack().copy();
        List<Slot> inventory = List.of();

        if (Util.isInventoryScreen()) {
            inventory = findPlayerInventory(slots);
        } else if (Util.isContainerScreen()) {
            inventory = findSelfInventory(slots, focusedSlot);
        }

        for (Slot slot : inventory) {
            if (ItemStack.areItemsAndComponentsEqual(focusedStack, slot.getStack())) {
                Util.dropOneStack(slot);
            }
        }
        return true;
    }

    public static boolean craftOne(List<Slot> slots, Slot focusedSlot) {
        if (!Util.isCraftingResultSlot(focusedSlot)) {
            return false;
        }

        List<Slot> inventory = findPlayerInventory(slots);
        clickSlots(inventory, focusedSlot);
        return true;
    }

    public static boolean buyOne(List<Slot> slots, Slot focusedSlot) {
        if (!Util.isTradeOutputSlot(focusedSlot)) {
            return false;
        }

        List<Slot> inventory = findPlayerInventory(slots);
        clickSlots(inventory, focusedSlot);
        return true;
    }
}
