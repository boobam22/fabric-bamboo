package bamboo.inventory.action;

import java.util.ArrayList;
import java.util.TreeMap;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.collection.DefaultedList;

public interface MergeActionInterface extends MoveActionInterface {
    private int findPlayerInventoryIndex() {
        for (Slot slot : getHandler().slots) {
            if (slot.inventory instanceof PlayerInventory && slot.getClass() == Slot.class) {
                return slot.id;
            }
        }
        return -1;
    }

    default void merge() {
        int start = findPlayerInventoryIndex();
        if (start == -1) {
            return;
        }

        merge(start, start + 27);

        ScreenHandler handler = getHandler();
        if (handler instanceof GenericContainerScreenHandler || handler instanceof ShulkerBoxScreenHandler) {
            merge(0, start);
        }
    }

    private void merge(int start, int end) {
        ScreenHandler handler = getHandler();
        DefaultedList<Slot> slots = handler.slots;

        TreeMap<Slot, ArrayList<Slot>> sortMap = new TreeMap<>((a, b) -> {
            if (ItemStack.areItemsAndComponentsEqual(a.getStack(), b.getStack())) {
                return 0;
            }
            return a.id - b.id;
        });
        for (int i = start; i < end; i++) {
            Slot slot = slots.get(i);
            if (slot.hasStack()) {
                sortMap.putIfAbsent(slot, new ArrayList<>());
                sortMap.get(slot).add(slot);
            }
        }

        TreeMap<Slot, Integer> diffMap = new TreeMap<>((a, b) -> a.id - b.id);
        int offset = start;
        for (Slot firstSlot : sortMap.keySet()) {
            ArrayList<Slot> mergableSlots = sortMap.get(firstSlot);
            offset = Math.max(offset, firstSlot.id);
            diffMap.put(firstSlot, offset);

            int maxCount = firstSlot.getStack().getMaxCount();
            int totalCount = mergableSlots.stream()
                    .map(it -> it.getStack().getCount())
                    .reduce(0, Integer::sum);
            offset += Math.ceilDiv(totalCount, maxCount);
        }

        for (Slot firstSlot : sortMap.reversed().keySet()) {
            ArrayList<Slot> mergableSlots = sortMap.get(firstSlot);
            int baseOffset = diffMap.get(firstSlot);
            offset = baseOffset;

            for (Slot from : mergableSlots) {
                if (from.id >= baseOffset && from.id <= offset) {
                    continue;
                }

                leftClick(from);
                leftClick(slots.get(offset));

                while (!handler.getCursorStack().isEmpty()) {
                    offset += 1;
                    leftClick(slots.get(offset));
                }
            }
        }
    }
}
