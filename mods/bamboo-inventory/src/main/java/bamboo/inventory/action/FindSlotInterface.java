package bamboo.inventory.action;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.CrafterScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface FindSlotInterface extends HandledScreenInterface {
    default List<Slot> findTargetSlots(Slot slot, int n) {
        ScreenHandler h = getHandler();

        if (h instanceof GenericContainerScreenHandler
                || h instanceof Generic3x3ContainerScreenHandler
                || h instanceof ShulkerBoxScreenHandler
                || h instanceof HopperScreenHandler) {
            return fromContainer(slot, n);
        } else if (h instanceof CrafterScreenHandler) {
            return fromCrafter(slot, n);
        }

        return null;
    }

    default List<Slot> findTargetSlots(Slot slot, int n, int start, int end) {
        if (n <= 0) {
            return List.of(slot);
        }

        List<Slot> targetSlots = new ArrayList<>();

        DefaultedList<Slot> slots = getHandler().slots;
        Slot empty = null;
        ItemStack stack = slot.getStack();
        boolean isStackable = stack.isStackable();

        for (int i = start; i < end; i++) {
            Slot slot2 = slots.get(i);
            ItemStack stack2 = slot2.getStack();

            if (stack2.isEmpty() && slot2.canInsert(stack)) {
                if (empty == null) {
                    empty = slot2;
                    if (!isStackable) {
                        break;
                    }
                }
                continue;
            }

            if (!isStackable) {
                continue;
            }

            if (ItemStack.areItemsAndComponentsEqual(stack, stack2)) {
                int size = stack2.getMaxCount() - stack2.getCount();
                if (size > 0) {
                    n -= size;
                    targetSlots.add(slot2);
                }

                if (n <= 0) {
                    break;
                }
            }
        }

        if (n > 0) {
            targetSlots.add(empty == null ? slot : empty);
        }

        return List.copyOf(targetSlots);
    }

    private List<Slot> fromContainer(Slot slot, int n) {
        int size = getHandler().slots.size();
        int boundary1 = size - 36;
        int boundary2 = size - 9;
        int start, end;

        if (slot.id < boundary1 || slot.id > boundary2) {
            start = boundary1;
            end = boundary2;
        } else {
            start = 0;
            end = boundary1;
        }

        return findTargetSlots(slot, n, start, end);
    }

    private List<Slot> fromCrafter(Slot slot, int n) {
        int start, end;

        if (slot.id < 9) {
            start = 9;
            end = 45;
        } else if (slot.id < 45) {
            start = 0;
            end = 9;
        } else {
            return null;
        }

        return findTargetSlots(slot, n, start, end);
    }
}
