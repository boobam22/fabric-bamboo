package bamboo.inventory.command;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ShulkerBoxScreenHandler extends net.minecraft.screen.ShulkerBoxScreenHandler {
    private int selfId = -1;

    public ShulkerBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Slot selfSlot) {
        super(syncId, playerInventory, inventory);
        for (Slot slot : this.slots) {
            if (slot.inventory instanceof PlayerInventory && selfSlot.getIndex() == slot.getIndex()) {
                selfId = slot.id;
                break;
            }
        }
    }

    @Override
    public void onSlotClick(int slotId, int button, SlotActionType actionType, PlayerEntity player) {
        if (selfId == slotId) {
            return;
        }
        super.onSlotClick(slotId, button, actionType, player);
    }
}
