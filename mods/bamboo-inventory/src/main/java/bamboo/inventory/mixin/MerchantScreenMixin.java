package bamboo.inventory.mixin;

import org.lwjgl.glfw.GLFW;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;

import bamboo.inventory.action.TradeAction;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin {
    @Shadow
    private int selectedIndex;

    @Redirect(method = "init", at = @At(value = "NEW", target = "Lnet/minecraft/client/gui/screen/ingame/MerchantScreen$WidgetButtonPage;"))
    private MerchantScreen.WidgetButtonPage init(MerchantScreen screen, int x, int y, int idx, PressAction onPress) {
        return new WidgetButtonPage(screen, x, y, idx, onPress);
    }

    private static class WidgetButtonPage extends MerchantScreen.WidgetButtonPage {
        private MerchantScreen screen;

        public WidgetButtonPage(MerchantScreen screen, int x, int y, int idx, PressAction onPress) {
            screen.super(x, y, idx, onPress);
            this.screen = screen;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                boolean success = super.mouseClicked(mouseX, mouseY, GLFW.GLFW_MOUSE_BUTTON_LEFT);
                if (success) {
                    int idx = ((MerchantScreenMixin) (Object) this.screen).selectedIndex;
                    TradeAction.buyAll(this.screen.getScreenHandler(), idx);
                }
                return success;
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
