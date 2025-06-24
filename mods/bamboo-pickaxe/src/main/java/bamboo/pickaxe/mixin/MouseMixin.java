package bamboo.pickaxe.mixin;

import org.lwjgl.glfw.GLFW;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Mouse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import bamboo.pickaxe.Pickaxe;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int modifiers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (window == client.getWindow().getHandle()
                && client.currentScreen == null
                && button == GLFW.GLFW_MOUSE_BUTTON_LEFT
                && action == GLFW.GLFW_PRESS
                && GLFW.glfwGetKey(window, GLFW.GLFW_KEY_B) != GLFW.GLFW_RELEASE) {
            Pickaxe.breakCooldown = Pickaxe.breakCooldown.next();
            Text text = Text.literal("Break Cooldown ").append(
                    Text.literal(Pickaxe.breakCooldown.toString()).formatted(Formatting.GREEN));
            client.inGameHud.setOverlayMessage(text, false);

            ci.cancel();
        }
    }
}
