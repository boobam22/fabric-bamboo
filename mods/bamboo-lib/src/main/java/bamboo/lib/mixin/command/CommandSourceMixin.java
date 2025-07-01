package bamboo.lib.mixin.command;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.command.CommandSource;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {
    @Inject(method = "shouldSuggest", at = @At("HEAD"), cancellable = true)
    private static void shouldSuggest(String remaining, String candidate, CallbackInfoReturnable<Boolean> cir) {
        int offset = 0;
        for (int i = 0; i < remaining.length(); i++) {
            offset = candidate.indexOf(remaining.charAt(i), offset) + 1;
            if (offset == 0) {
                cir.setReturnValue(false);
                return;
            }
        }
        cir.setReturnValue(true);
    }
}
