package bamboo.inventory.action;

import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.VillagerEntity;

import bamboo.inventory.mixin.MerchantScreenHandlerAccessor;

public class TradeAction {
    public static boolean refresh(PlayerEntity player, Slot slot) {
        if (slot instanceof TradeOutputSlot && !slot.hasStack()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                MerchantScreenHandler handler = (MerchantScreenHandler) serverPlayer.currentScreenHandler;
                if (handler.getExperience() == 0) {
                    VillagerEntity villager = (VillagerEntity) ((MerchantScreenHandlerAccessor) handler)
                            .bamboo_getMerchant();
                    villager.setOffers(null);
                    serverPlayer.sendTradeOffers(handler.syncId, villager.getOffers(),
                            villager.getVillagerData().level(), villager.getExperience(),
                            villager.isLeveledMerchant(), villager.canRefreshTrades());
                }
            }
            return true;
        }
        return false;
    }

    public static void buyAll(MerchantScreenHandler handler, int idx) {
        Slot input1 = handler.getSlot(0);
        Slot output = handler.getSlot(2);

        while (true) {
            int count = input1.getStack().getCount();
            MoveAction.craftOne(handler, handler.slots, output);
            if (input1.getStack().getCount() == count) {
                handler.switchTo(idx);
                if (input1.getStack().getCount() == count) {
                    break;
                }
            }
        }
    }
}
