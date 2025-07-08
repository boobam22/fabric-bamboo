package bamboo.world.data;

import java.util.List;
import java.util.TreeMap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;

public class PointManager extends PersistentState {
    private static final String id = "bamboo-world-points";
    public static final Codec<PointManager> CODEC;
    public static final PersistentStateType<PointManager> TYPE;
    private final TreeMap<String, Point> points = new TreeMap<>();

    private PointManager() {
        add("home", 0, 0, World.OVERWORLD);
    }

    private PointManager(List<Point> points) {
        points.forEach(point -> {
            this.points.put(point.name(), point);
        });
    }

    public List<Point> getAll() {
        return List.copyOf(points.values());
    }

    public boolean add(String name, int x, int z, RegistryKey<World> worldKey) {
        this.markDirty();
        return points.put(name, new Point(name, x, z, worldKey)) == null;
    }

    public boolean remove(String name) {
        this.markDirty();
        return points.remove(name) != null;
    }

    public static PointManager get(ServerWorld world) {
        return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(TYPE);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Point.CODEC.listOf().fieldOf("points").forGetter(PointManager::getAll))
                .apply(instance, PointManager::new));
        TYPE = new PersistentStateType<>(id, PointManager::new, CODEC, null);
    }
}
