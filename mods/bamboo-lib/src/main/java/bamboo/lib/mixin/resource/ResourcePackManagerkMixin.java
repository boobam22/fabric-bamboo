package bamboo.lib.mixin.resource;

import java.util.Set;
import java.util.HashSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;

import bamboo.lib.resource.ModResourcePackProvider;

@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerkMixin {
    @Mutable
    @Final
    @Shadow
    private Set<ResourcePackProvider> providers;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        Set<ResourcePackProvider> hs = new HashSet<>(this.providers);
        hs.add(new ModResourcePackProvider());
        this.providers = Set.copyOf(hs);
    }
}
