package bamboo.world.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.ColumnPos;

public record RegionPos(int x, int z) implements Comparable<RegionPos> {
    public static final Codec<RegionPos> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(RegionPos::x),
            Codec.INT.fieldOf("z").forGetter(RegionPos::z)).apply(instance, RegionPos::new));

    public ColumnPos getCenter() {
        return new ColumnPos((x << 9) + 256, (z << 9) + 256);
    }

    @Override
    public int compareTo(RegionPos other) {
        int cmp = Integer.compare(x, other.x);
        return cmp == 0 ? Integer.compare(z, other.z) : cmp;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", x, z);
    }
}
