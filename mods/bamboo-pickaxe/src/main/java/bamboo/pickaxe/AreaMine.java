package bamboo.pickaxe;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;

public class AreaMine {
    private static final int LIMIT = 32;
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

    public void resetArea(BlockPos pos) {
        area = new Box(pos);
    }

    public void mine(ServerWorld world, BlockPos pos) {
        Box newArea = area.union(new Box(pos));
        if (area == newArea
                || newArea.getLengthX() - area.getLengthX() > LIMIT
                || newArea.getLengthY() - area.getLengthY() > LIMIT
                || newArea.getLengthZ() - area.getLengthZ() > LIMIT) {
            return;
        }

        BlockPos.stream(newArea)
                .filter(bp -> !area.contains(bp.getX(), bp.getY(), bp.getZ()))
                .forEach(bp -> world.setBlockState(bp, Blocks.AIR.getDefaultState(), 950));
        area = newArea;
    }
}
