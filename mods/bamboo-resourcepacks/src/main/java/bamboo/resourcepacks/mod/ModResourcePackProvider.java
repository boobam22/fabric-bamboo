package bamboo.resourcepacks.mod;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.nio.file.Path;
import java.nio.file.Files;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourcePackCompatibility;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.text.Text;

public class ModResourcePackProvider implements ResourcePackProvider {
    public static final ModResourcePack RESOURCE_PACK;
    public static final ResourcePackProfile RESOURCE_PACK_PROFILE;

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        profileAdder.accept(RESOURCE_PACK_PROFILE);
    }

    static {
        String baseID = "bamboo-resourcepacks";
        Text baseTitle = Text.literal("Bamboo Resourcepacks");
        Text baseDesc = Text.literal("This is description");

        HashMap<String, Path> rootPaths = new HashMap<>();
        HashMap<ResourceType, Set<String>> namespaces = new HashMap<>();
        namespaces.put(ResourceType.CLIENT_RESOURCES, new HashSet<>());
        namespaces.put(ResourceType.SERVER_DATA, new HashSet<>());

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            String id = mod.getMetadata().getId();

            if (id.startsWith("bamboo-")) {
                Path root = mod.getRootPaths().get(0);

                for (ResourceType type : ResourceType.values()) {
                    Path path = root.resolve(type.getDirectory());

                    if (Files.exists(path.resolve(id))) {
                        rootPaths.putIfAbsent(id, root);
                        namespaces.get(type).add(id);
                    }

                    if (id.equals(baseID) && Files.exists(path.resolve("minecraft"))) {
                        rootPaths.putIfAbsent("minecraft", root);
                        namespaces.get(type).add("minecraft");
                    }
                }
            }
        }

        ResourcePackInfo info = new ResourcePackInfo(baseID, baseTitle, ResourcePackSource.BUILTIN, Optional.empty());
        ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(baseDesc,
                ResourcePackCompatibility.COMPATIBLE, FeatureSet.empty(), List.of());
        ResourcePackPosition position = new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.TOP,
                false);

        RESOURCE_PACK = new ModResourcePack(info, baseID, namespaces, rootPaths);
        RESOURCE_PACK_PROFILE = new ResourcePackProfile(info, new ModResourcePackFactory(), metadata, position);
    }
}
