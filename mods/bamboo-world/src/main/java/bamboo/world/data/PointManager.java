package bamboo.world.data;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ChunkPos;

public class PointManager extends PersistentState {
    private static final String id = "bamboo-world-points";
    public static final Codec<PointManager> CODEC;
    public static final PersistentStateType<PointManager> TYPE;
    private final TreeMap<String, Point> points = new TreeMap<>();

    private PointManager() {
        add("spawn", 0, 0, World.OVERWORLD);
        add("end", 0, 0, World.END);
    }

    private PointManager(List<Point> points) {
        points.forEach(point -> {
            this.points.put(point.name(), point);
        });
    }

    public List<Point> getAll() {
        return List.copyOf(points.values());
    }

    public Point get(String name) {
        return points.getOrDefault(name, null);
    }

    public boolean add(String name, int x, int z, RegistryKey<World> worldKey) {
        this.markDirty();
        return points.put(name, new Point(name, x, z, worldKey)) == null;
    }

    public boolean remove(String name) {
        this.markDirty();
        return points.remove(name) != null;
    }

    public Map<RegistryKey<World>, Set<Long>> getRegions() {
        Map<RegistryKey<World>, Set<Long>> regions = new HashMap<>();
        points.values().forEach(point -> {
            regions.putIfAbsent(point.worldKey(), new HashSet<>());
            Set<Long> set = regions.get(point.worldKey());
            set.add(ChunkPos.toLong(point.x() - 256 >> 9, point.z() - 256 >> 9));
            set.add(ChunkPos.toLong(point.x() - 256 >> 9, point.z() + 256 >> 9));
            set.add(ChunkPos.toLong(point.x() + 256 >> 9, point.z() - 256 >> 9));
            set.add(ChunkPos.toLong(point.x() + 256 >> 9, point.z() + 256 >> 9));
        });
        return regions;
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
