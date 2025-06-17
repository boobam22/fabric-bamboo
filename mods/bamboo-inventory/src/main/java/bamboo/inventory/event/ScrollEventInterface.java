package bamboo.inventory.event;

import java.util.List;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.screen.slot.Slot;

import bamboo.inventory.action.MoveActionInterface;
import bamboo.inventory.action.FindSlotInterface;

public interface ScrollEventInterface extends MoveActionInterface, FindSlotInterface {
    private int findPlayerInventoryIndex() {
        for (Slot slot : getHandler().slots) {
            if (slot.inventory instanceof PlayerInventory && slot.getClass() == Slot.class) {
                return slot.id;
            }
        }
        return -1;
    }

    default boolean onScroll() {
        Slot slot = getFocusedSlot();

        if (slot instanceof CraftingResultSlot || slot instanceof TradeOutputSlot) {
            int start = findPlayerInventoryIndex();
            List<Slot> to = findTargetSlots(slot, slot.getStack().getCount(), start, start + 27);
            if (to.getLast() != slot) {
                moveStack(slot, to);
            }
        } else {
            List<Slot> to = findTargetSlots(slot, 1);
            if (to != null) {
                moveOne(slot, to.get(0));
            }
        }

        return true;
    }
}
