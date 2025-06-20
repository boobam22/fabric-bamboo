package bamboo.inventory.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Comparator;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.Registries;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;
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
        if (!handler.getCursorStack().isEmpty()) {
            return;
        }

        ArrayList<ArrayList<Slot>> groupedSlot = new ArrayList<>();
        HashMap<Integer, ItemStack> input = new HashMap<>();
        HashMap<Integer, ItemStack> output = new HashMap<>();
        TreeMap<ItemStack, ArrayList<Slot>> stackMap = new TreeMap<>(Comparator
                .comparing((ItemStack stack) -> stack.getItem().hashCode())
                .thenComparing((ItemStack stack) -> stack.getComponents().hashCode()));

        for (int i = start; i < end; i++) {
            Slot slot = slots.get(i);
            ItemStack stack = slot.getStack();
            if (slot.hasStack()) {
                if (!stackMap.containsKey(stack)) {
                    ArrayList<Slot> group = new ArrayList<>();
                    groupedSlot.add(group);
                    stackMap.put(stack, group);
                }
                stackMap.get(stack).add(slot);
                input.put(i, stack.copy());
            }
        }

        int offset = start;
        for (ArrayList<Slot> group : groupedSlot) {
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

        ArrayList<Integer> points = new ArrayList<>(input.keySet());
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

        ArrayList<Integer> path = new ArrayList<>();
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

        for (int id : path) {
            Slot slot = slots.get(id);
            ItemStack stack = slot.getStack();
            ItemStack cursorStack = handler.getCursorStack();

            TagKey<Item> bundleKey = TagKey.of(Registries.ITEM.getKey(), Identifier.ofVanilla("bundles"));
            if (stack.isIn(bundleKey) && !cursorStack.isEmpty()
                    || cursorStack.isIn(bundleKey) && !stack.isEmpty()) {
                rightClick(slot);
            } else {
                leftClick(slot);
            }
        }
    }
}
