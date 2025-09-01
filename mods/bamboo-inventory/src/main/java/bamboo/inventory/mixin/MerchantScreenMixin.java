package bamboo.inventory.mixin;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.screen.slot.Slot;

import bamboo.inventory.action.MoveAction;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin {
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
                Runnable select = () -> super.mouseClicked(mouseX, mouseY, GLFW.GLFW_MOUSE_BUTTON_LEFT);

                List<Slot> slots = this.screen.getScreenHandler().slots;
                Slot input = slots.get(0);
                Slot output = slots.get(2);
                while (true) {
                    select.run();
                    if (!output.hasStack()) {
                        break;
                    }

                    while (output.hasStack()) {
                        int n = input.getStack().getCount();
                        MoveAction.buyOne(slots, output);
                        if (n == input.getStack().getCount()) {
                            return true;
                        }
                    }
                }
                return true;
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
