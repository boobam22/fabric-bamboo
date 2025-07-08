package bamboo.world.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;

public record Point(String name, int x, int z, RegistryKey<World> worldKey) {
    public static final Codec<Point> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Point::name),
            Codec.INT.fieldOf("x").forGetter(Point::x),
            Codec.INT.fieldOf("z").forGetter(Point::z),
            RegistryKey.createCodec(World.OVERWORLD.getRegistryRef()).fieldOf("world").forGetter(Point::worldKey))
            .apply(instance, Point::new));

    @Override
    public String toString() {
        return String.format("%s [%d, %d] in %s", name, x, z, worldKey.getValue());
    }
}
