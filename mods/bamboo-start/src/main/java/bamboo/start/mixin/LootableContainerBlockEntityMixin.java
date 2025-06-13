package bamboo.start.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

@Mixin(LootableContainerBlockEntity.class)
public abstract class LootableContainerBlockEntityMixin {
    @Inject(method = "getLootTable", at = @At("RETURN"), cancellable = true)
    private void getLootTable(CallbackInfoReturnable<RegistryKey<LootTable>> cir) {
        String path = "chests/spawn_bonus_chest";
        Identifier id1 = Identifier.ofVanilla(path);
        Identifier id2 = Identifier.of("bamboo-start", path);

        RegistryKey<LootTable> lootTable = cir.getReturnValue();
        if (lootTable != null && lootTable.getValue().compareTo(id1) == 0) {
            cir.setReturnValue(RegistryKey.of(RegistryKeys.LOOT_TABLE, id2));
        }
    }
}