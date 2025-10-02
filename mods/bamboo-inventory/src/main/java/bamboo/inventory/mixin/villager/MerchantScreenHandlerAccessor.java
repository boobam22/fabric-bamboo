package bamboo.inventory.mixin.villager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.village.Merchant;

@Mixin(MerchantScreenHandler.class)
public interface MerchantScreenHandlerAccessor {
    @Accessor("merchant")
    Merchant bamboo_getMerchant();
}