package bamboo.inventory.command;

import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class CraftingScreenHandler extends net.minecraft.screen.CraftingScreenHandler {
    CraftingScreenHandler(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        super(syncId, playerInventory, ScreenHandlerContext.create(player.getWorld(), player.getBlockPos()));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
