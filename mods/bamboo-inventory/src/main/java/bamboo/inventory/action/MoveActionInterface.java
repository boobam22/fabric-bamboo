package bamboo.inventory.action;

import java.util.List;

import net.minecraft.screen.slot.Slot;

public interface MoveActionInterface extends BaseActionInterface {

    default void moveStack(Slot from, List<Slot> to) {
        if (to.size() == 1 && from == to.get(0)) {
            return;
        }

        leftClick(from);
        for (Slot slot : to) {
            leftClick(slot);
        }
    }

    default void moveStack(Slot from, Slot to) {
        moveStack(from, List.of(to));
    }

    default void moveLeaveOne(Slot from, List<Slot> to) {
        if (to.size() == 1 && from == to.get(0)) {
            return;
        }

        leftClick(from);
        rightClick(from);
        for (Slot slot : to) {
            leftClick(slot);
        }
    }

    default void moveLeaveOne(Slot from, Slot to) {
        moveLeaveOne(from, List.of(to));
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
