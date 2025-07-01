package bamboo.pickaxe;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.BlockHitResult;

public class AreaMine {
    private boolean enabled;
    private Box area;

    public void toggle() {
        enabled ^= true;
        area = null;
    }

    public boolean toggle(MinecraftClient client) {
        if (client.currentScreen != null) {
            return false;
        }
        toggle();

        Text text = Text.literal("Area Mine ")
                .append(Text.literal(String.valueOf(enabled)).formatted(enabled ? Formatting.GREEN : Formatting.RED));
        client.inGameHud.setOverlayMessage(text, false);
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Box getArea() {
        return area;
    }

    public void resetArea() {
        HitResult hit = MinecraftClient.getInstance().crosshairTarget;
        if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
            area = new Box(((BlockHitResult) hit).getBlockPos());
        } else {
            area = null;
        }
    }

    public void mine(BlockPos pos) {
    }
}
