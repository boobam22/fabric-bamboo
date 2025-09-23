package bamboo.inventory.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import bamboo.inventory.Inventory;

@Mixin(targets = "net.minecraft.village.TradeOffers$EnchantBookFactory")
public abstract class EnchantBookFactoryMixin {
    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"))
    private int create(Random random, int min, int max) {
        if (Inventory.villagerMaxBooks.getValue()) {
            return max;
        }
        return MathHelper.nextInt(random, min, max);
    }
}
