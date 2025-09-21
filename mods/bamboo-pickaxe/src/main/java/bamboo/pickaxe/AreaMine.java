package bamboo.pickaxe;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;

public class AreaMine {
    private boolean enabled;
    private Box area;

    public void toggle() {
        enabled ^= true;
        area = null;
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

    public void expandArea(BlockPos pos) {
        if (area == null) {
            resetArea(pos);
        } else {
            area = area.union(new Box(pos));
        }
    }
}
