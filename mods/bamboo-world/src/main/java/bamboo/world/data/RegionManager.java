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

    public RegionManager() {
        regions.add(new RegionPos(0, 0));
        regions.add(new RegionPos(0, -1));
        regions.add(new RegionPos(-1, 0));
        regions.add(new RegionPos(-1, -1));
        this.markDirty();
    }

    public RegionManager(List<RegionPos> regions) {
        this.regions.addAll(regions);
    }

    public List<RegionPos> getAll() {
        return List.copyOf(regions);
    }

    public boolean add(RegionPos regionPos) {
        this.markDirty();
        return regions.add(regionPos);
    }

    public boolean remove(RegionPos regionPos) {
        this.markDirty();
        return regions.remove(regionPos);
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
