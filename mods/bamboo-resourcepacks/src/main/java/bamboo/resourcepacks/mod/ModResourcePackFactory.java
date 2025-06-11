package bamboo.resourcepacks.mod;

import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackProfile;

public class ModResourcePackFactory implements ResourcePackProfile.PackFactory {
    @Override
    public ResourcePack open(ResourcePackInfo info) {
        return ModResourcePackProvider.RESOURCE_PACK;
    }

    @Override
    public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
        return ModResourcePackProvider.RESOURCE_PACK;
    }
}
