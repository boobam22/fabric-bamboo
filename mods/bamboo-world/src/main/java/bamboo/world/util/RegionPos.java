package bamboo.world.util;

import net.minecraft.util.math.ColumnPos;

public record RegionPos(int x, int z) {
    @Override
    public String toString() {
        return String.format("[%d, %d]", x, z);
    }

    public ColumnPos getCenter() {
        return new ColumnPos((x << 9) + 256, (z << 9) + 256);
    }
}
