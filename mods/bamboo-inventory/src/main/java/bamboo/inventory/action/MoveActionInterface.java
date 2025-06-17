package bamboo.inventory.action;

import java.util.List;

import net.minecraft.screen.slot.Slot;

public interface MoveActionInterface extends BaseActionInterface {
    private boolean shouldSkip(Slot from, List<Slot> to) {
        return to.size() == 1 && from == to.get(0);
    }

    default void moveStack(Slot from, List<Slot> to) {
        if (shouldSkip(from, to)) {
            return;
        }

        leftClick(from);
        for (Slot slot : to) {
            leftClick(slot);
        }
    }

    default void moveLeaveOne(Slot from, List<Slot> to) {
        if (shouldSkip(from, to)) {
            return;
        }

        leftClick(from);
        rightClick(from);
        for (Slot slot : to) {
            leftClick(slot);
        }
    }

    default void moveOne(Slot from, Slot to) {
        if (from == to) {
            return;
        }

        leftClick(from);
        rightClick(to);
        leftClick(from);
    }
}
