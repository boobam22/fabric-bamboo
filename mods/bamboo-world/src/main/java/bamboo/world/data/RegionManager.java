package bamboo.world.data;

import java.util.List;
import java.util.TreeSet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.server.world.ServerWorld;

public class RegionManager extends PersistentState {
    private static final String id = "bamboo-world-regions";
    public static final Codec<RegionManager> CODEC;
    public static final PersistentStateType<RegionManager> TYPE;
    private final TreeSet<RegionPos> regions = new TreeSet<>();

    private RegionManager() {
        add(0, 0);
        add(0, -1);
        add(-1, 0);
        add(-1, -1);
    }

    private RegionManager(List<RegionPos> regions) {
        this.regions.addAll(regions);
    }

    public List<RegionPos> getAll() {
        return List.copyOf(regions);
    }

    public boolean add(int x, int z) {
        this.markDirty();
        return regions.add(new RegionPos(x, z));
    }

    public boolean remove(int x, int z) {
        this.markDirty();
        return regions.remove(new RegionPos(x, z));
    }

    public boolean contains(int x, int z) {
        return regions.contains(new RegionPos(x, z));
    }

    public static RegionManager get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RegionPos.CODEC.listOf().fieldOf("regions").forGetter(RegionManager::getAll))
                .apply(instance, RegionManager::new));
        TYPE = new PersistentStateType<>(id, RegionManager::new, CODEC, null);
    }
}
