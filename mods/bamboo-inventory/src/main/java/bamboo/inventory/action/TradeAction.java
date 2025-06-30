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

    public static void buyAll(MerchantScreenHandler handler, Runnable select) {
        Slot output = handler.getSlot(2);

        while (true) {
            select.run();
            if (!output.hasStack()) {
                break;
            }

            while (output.hasStack()) {
                MoveAction.craftOrBuyOne(handler, handler.slots, output);
            }
        }
    }
}
