package bamboo.inventory.command;

import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.entity.player.PlayerEntity;

public class InventoryCraftingScreenHandler extends CraftingScreenHandler {
    InventoryCraftingScreenHandler(int syncId, PlayerEntity player) {
        super(syncId, player.getInventory(), ScreenHandlerContext.create(player.getWorld(), player.getBlockPos()));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
