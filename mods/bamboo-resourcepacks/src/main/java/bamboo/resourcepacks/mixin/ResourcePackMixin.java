package bamboo.resourcepacks.mixin;

import java.util.Set;
import java.util.HashSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;

import bamboo.resourcepacks.mod.ModResourcePackProvider;

@Mixin(ResourcePackManager.class)
public abstract class ResourcePackMixin {
    @Mutable
    @Shadow
    @Final
    private Set<ResourcePackProvider> providers;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourcePackProvider[] providers, CallbackInfo ci) {
        HashSet<ResourcePackProvider> hs = new HashSet<>(this.providers);
        hs.add(new ModResourcePackProvider());
        this.providers = Set.copyOf(hs);
    }
}
