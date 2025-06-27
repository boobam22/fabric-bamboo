package bamboo.lib.resource;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProfile.Metadata;
import net.minecraft.resource.ResourcePackProfile.InsertionPosition;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourcePackCompatibility;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;

public class ModResourcePackProvider implements ResourcePackProvider {
    public static final ModResourcePack RESOURCE_PACK;
    public static final ResourcePackProfile RESOURCE_PACK_PROFILE;

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        profileAdder.accept(RESOURCE_PACK_PROFILE);
    }

    public static class PackFactory implements ResourcePackProfile.PackFactory {
        @Override
        public ResourcePack open(ResourcePackInfo info) {
            return RESOURCE_PACK;
        }

        @Override
        public ResourcePack openWithOverlays(ResourcePackInfo info, Metadata metadata) {
            return RESOURCE_PACK;
        }
    }

    static {
        String id = "bamboo-lib";
        Text title = Text.of("Bamboo Lib");
        Text desc = Text.of("This is description");

        ResourcePackInfo info = new ResourcePackInfo(id, title, ResourcePackSource.BUILTIN, Optional.empty());
        Metadata metadata = new Metadata(desc, ResourcePackCompatibility.COMPATIBLE, FeatureSet.empty(), List.of());
        ResourcePackPosition position = new ResourcePackPosition(true, InsertionPosition.TOP, false);

        RESOURCE_PACK = new ModResourcePack(info);
        RESOURCE_PACK_PROFILE = new ResourcePackProfile(info, new PackFactory(), metadata, position);
    }
}
