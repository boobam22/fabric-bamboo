package bamboo.inventory.action2;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Comparator;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public class MergeAction {
    public static void merge(ScreenHandler handler, List<Slot> slots, Slot focusedSlot) {
        List<Slot> inventory = Util.findPlayerInventory(slots);
        if (inventory.size() > 0) {
            mergeInventory(handler, slots, inventory);
        }

        if (Util.isChestScreen(handler)) {
            mergeInventory(handler, slots, slots.subList(0, slots.size() - 36));
        }
    }

    private static void mergeInventory(ScreenHandler handler, List<Slot> slots, List<Slot> inventory) {
        List<List<Slot>> groupedSlot = new ArrayList<>();
        TreeMap<Integer, ItemStack> input = new TreeMap<>();
        TreeMap<Integer, ItemStack> output = new TreeMap<>();
        TreeMap<ItemStack, List<Slot>> stackMap = new TreeMap<>(Comparator
                .comparing((ItemStack stack) -> stack.getItem().hashCode())
                .thenComparing((ItemStack stack) -> stack.getComponents().hashCode()));

        for (Slot slot : inventory) {
            ItemStack stack = slot.getStack();
            if (slot.hasStack()) {
                if (!stackMap.containsKey(stack)) {
                    List<Slot> group = new ArrayList<>();
                    groupedSlot.add(group);
                    stackMap.put(stack, group);
                }
                stackMap.get(stack).add(slot);
                input.put(slot.id, stack.copy());
            }
        }

        int offset = -1;
        for (List<Slot> group : groupedSlot) {
            Slot firstSlot = group.getFirst();
            offset = Math.max(offset, firstSlot.id);
            ItemStack stack = firstSlot.getStack();
            int maxCount = stack.getMaxCount();
            int totalCount = group.stream().map(slot -> slot.getStack().getCount()).reduce(0, Integer::sum);
            int nStack = totalCount / maxCount;
            int remain = totalCount % maxCount;

            for (int i = 0; i < nStack; i++) {
                output.put(offset++, stack.copyWithCount(maxCount));
            }
            if (remain > 0) {
                output.put(offset++, stack.copyWithCount(remain));
            }
        }

        List<Integer> points = new ArrayList<>(input.keySet());
        TreeMap<ItemStack, TreeMap<Integer, Integer>> state = new TreeMap<>(stackMap.comparator());
        for (int id : output.keySet()) {
            ItemStack stack = output.get(id);
            int count = stack.getCount();
            if (points.contains(id)
                    && ItemStack.areItemsAndComponentsEqual(input.get(id), stack)
                    && input.get(id).getCount() <= count) {
                points.remove(Integer.valueOf(id));
                count -= input.get(id).getCount();
            }
            if (count > 0) {
                state.putIfAbsent(stack, new TreeMap<>());
                state.get(stack).put(id, count);
            }
        }

        List<Integer> path = new ArrayList<>();
        while (points.size() > 0) {
            int id = points.removeFirst();
            ItemStack cursorStack = input.get(id).copy();
            path.add(id);

            while (true) {
                TreeMap<Integer, Integer> offsetMap = state.get(cursorStack);
                id = offsetMap.firstKey();
                path.add(id);

                int cap = offsetMap.get(id);
                int cursorCount = cursorStack.getCount();
                if (cursorCount >= cap) {
                    offsetMap.remove(id);
                    cursorStack.decrement(cap);
                } else {
                    offsetMap.merge(id, -cursorCount, Integer::sum);
                    cursorStack = ItemStack.EMPTY;
                }

                if (points.contains(id)) {
                    points.remove(Integer.valueOf(id));
                    cursorStack = input.get(id).copy();
                }

                if (cursorStack.isEmpty()) {
                    break;
                }
            }
        }

        if (!handler.getCursorStack().isEmpty()) {
            Slot cursor = null;
            ItemStack stack = handler.getCursorStack();

            for (Slot slot : slots) {
                if (!slot.hasStack() && slot.canInsert(stack) && !path.contains(slot.id)) {
                    cursor = slot;
                    break;
                }
            }

            if (cursor == null) {
                return;
            } else {
                Util.leftClick(cursor);
                path.add(cursor.id);
            }
        }

        for (int id : path) {
            Slot slot = slots.get(id);
            ItemStack stack = slot.getStack();
            ItemStack cursorStack = handler.getCursorStack();

            if (stack.isIn(ItemTags.BUNDLES) && !cursorStack.isEmpty()
                    || cursorStack.isIn(ItemTags.BUNDLES) && !stack.isEmpty()) {
                Util.rightClick(slot);
            } else {
                Util.leftClick(slot);
            }
        }
    }
}
