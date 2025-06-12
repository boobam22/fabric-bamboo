package bamboo.start.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

@Mixin(WorldCreator.class)
public abstract class WorldCreatorMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        WorldCreator self = (WorldCreator) (Object) this;
        self.setWorldName("world");
        self.setDifficulty(Difficulty.HARD);
        self.setBonusChestEnabled(true);
        self.getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE).set(0, null);
        self.update();
    }
}
