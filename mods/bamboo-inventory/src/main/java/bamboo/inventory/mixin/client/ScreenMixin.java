package bamboo.inventory.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        System.out.println(self.getClass());
    }
}
