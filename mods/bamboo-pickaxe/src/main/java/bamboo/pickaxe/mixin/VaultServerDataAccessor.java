package bamboo.pickaxe.mixin;

import java.util.Set;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.vault.VaultServerData;

@Mixin(VaultServerData.class)
public interface VaultServerDataAccessor {
    @Accessor("rewardedPlayers")
    Set<UUID> bamboo_getRewardedPlayers();
}
