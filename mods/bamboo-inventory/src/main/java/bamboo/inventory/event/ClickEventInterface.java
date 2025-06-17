package bamboo.inventory.event;

import java.util.List;

import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerInventory;

import bamboo.inventory.action.MoveActionInterface;
import bamboo.inventory.action.FindSlotInterface;
import bamboo.inventory.action.MergeActionInterface;

public interface ClickEventInterface extends MoveActionInterface, FindSlotInterface, MergeActionInterface {
    default boolean onShiftClick() {
        return onShiftClick(getFocusedSlot());
    }

    private boolean onShiftClick(Slot slot) {
        if (slot.inventory instanceof PlayerInventory) {
            shiftClick(slot);
            return true;
        }

        List<Slot> to = findTargetSlots(slot, slot.getStack().getCount());
        if (to != null) {
            moveStack(slot, to);
            return true;
        }
        return false;
    }

    default boolean onShiftRightClick() {
        Slot slot = getFocusedSlot();
        List<Slot> to = findTargetSlots(slot, slot.getStack().getCount() - 1);
        if (to != null) {
            moveLeaveOne(slot, to);
            return true;
        }
        return false;
    }

    default boolean onAltClick() {
        Slot focusedSlot = getFocusedSlot();
        List<Slot> to = findTargetSlots(focusedSlot, focusedSlot.getStack().getCount());
        if (to == null) {
            return false;
        }

        for (Slot slot : getHandler().slots) {
            if (slot != focusedSlot
                    && slot.inventory == focusedSlot.inventory
                    && slot.hasStack()
                    && ItemStack.areItemsAndComponentsEqual(slot.getStack(), focusedSlot.getStack())) {
                onShiftClick(slot);
            }
        }
        onShiftClick(focusedSlot);
        return true;
    }

    default boolean onCtrolClick() {
        merge();
        return true;
    }
}
