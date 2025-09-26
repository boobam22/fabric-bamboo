package bamboo.render;

import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

public class Render implements ModInitializer {
    public static TagKey<EntityType<?>> HIGHLIGHT_RENDER;
    public static TagKey<EntityType<?>> DISABLE_RENDER;

    @Override
    public void onInitialize() {
    }

    static {
        HIGHLIGHT_RENDER = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("bamboo-render:highlight_render"));
        DISABLE_RENDER = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("bamboo-render:disable_render"));
    }
}
