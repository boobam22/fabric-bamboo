package bamboo.camera.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import bamboo.camera.ClientCamera;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @ModifyVariable(method = "renderHotbar", at = @At("STORE"), ordinal = 0)
    private PlayerEntity renderHotbar(PlayerEntity player) {
        if (ClientCamera.cameraController.isActive()) {
            return MinecraftClient.getInstance().player;
        }
        return player;
    }
}
