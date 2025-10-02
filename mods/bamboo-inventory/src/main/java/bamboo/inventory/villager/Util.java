package bamboo.inventory.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;

import bamboo.inventory.mixin.villager.MerchantEntityAccessor;
import bamboo.inventory.mixin.villager.MerchantScreenHandlerAccessor;

public class Util {
    public static void refreshTrade(ServerPlayerEntity player) {
        VillagerEntity villager = (VillagerEntity) ((MerchantScreenHandlerAccessor) player.currentScreenHandler)
                .bamboo_getMerchant();
        int exp = villager.getExperience();
        VillagerData villagerData = villager.getVillagerData();
        int level = villagerData.level();
        RegistryEntry<VillagerProfession> profession = villagerData.profession();

        if ((exp == VillagerData.getUpperLevelExperience(level - 1))
                && !profession.matchesKey(VillagerProfession.NONE)) {
            RegistryKey<VillagerProfession> professionKey = profession.getKey().orElse(VillagerProfession.NONE);

            Int2ObjectMap<TradeOffers.Factory[]> factory = null;
            if (villager.getWorld().getEnabledFeatures().contains(FeatureFlags.TRADE_REBALANCE)) {
                factory = TradeOffers.REBALANCED_PROFESSION_TO_LEVELED_TRADE.get(professionKey);
            }
            if (factory == null) {
                factory = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(professionKey);
            }
            TradeOffers.Factory[] pool = factory.get(level);

            TradeOfferList offers = villager.getOffers();
            offers.subList(offers.size() - Math.min(2, pool.length), offers.size()).clear();
            ((MerchantEntityAccessor) villager).bamboo_fillRecipesFromPool(offers, pool, 2);
            player.sendTradeOffers(player.currentScreenHandler.syncId, offers, level, exp,
                    villager.isLeveledMerchant(), villager.canRefreshTrades());
        }
    }
}
