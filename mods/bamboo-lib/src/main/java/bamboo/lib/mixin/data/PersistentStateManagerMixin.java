package bamboo.lib.mixin.data;

import com.mojang.datafixers.DataFixer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.PersistentStateManager;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;

@Mixin(PersistentStateManager.class)
public abstract class PersistentStateManagerMixin {
    @Redirect(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/datafixer/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/NbtCompound;II)Lnet/minecraft/nbt/NbtCompound;"))
    private NbtCompound readNbt(DataFixTypes types, DataFixer fixer, NbtCompound nbt, int v1, int v2) {
        if (types == null) {
            return nbt;
        }
        return types.update(fixer, nbt, v1, v2);
    }
}
