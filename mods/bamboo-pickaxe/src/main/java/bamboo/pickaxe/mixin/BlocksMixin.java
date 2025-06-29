package bamboo.pickaxe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.Settings;

@Mixin(Blocks.class)
public abstract class BlocksMixin {
    @Inject(method = "register(Ljava/lang/String;Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/Block;", at = @At("HEAD"))
    private static void register(String id, Settings settings, CallbackInfoReturnable<Block> cir) {
        if (id.equals("bedrock")) {
            settings.requiresTool().hardness(100);
        }
    }
}
