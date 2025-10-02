package bamboo.inventory.mixin.villager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.village.TradeOffer;

import bamboo.inventory.Inventory;

@Mixin(TradeOffer.class)
public abstract class TradeOfferMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(CallbackInfo ci) {
        if (Inventory.disableTradeLock.getValue()) {
            ci.cancel();
        }
    }
}
