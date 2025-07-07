package bamboo.world.util;

import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ColumnPos;

public record Point(String name, int x, int z, RegistryKey<World> worldKey) {
    public Point(String name, ColumnPos pos, RegistryKey<World> worldKey) {
        this(name, pos.x(), pos.z(), worldKey);
    }

    public Point(String name, int x, int z) {
        this(name, x, z, World.OVERWORLD);
    }

    public Point(String name, ColumnPos pos) {
        this(name, pos.x(), pos.z());
    }

    @Override
    public String toString() {
        return String.format("%s[%d, %d] in %s", name, x, z, worldKey.getValue());
    }
}
