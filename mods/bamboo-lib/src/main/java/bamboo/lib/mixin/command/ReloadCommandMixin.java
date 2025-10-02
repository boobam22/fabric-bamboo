package bamboo.lib.mixin.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(ReloadCommand.class)
public abstract class ReloadCommandMixin {
    @ModifyArg(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;", remap = false))
    private static LiteralArgumentBuilder<ServerCommandSource> register(
            LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.requires(source -> !source.getServer().isDedicated() || source.hasPermissionLevel(2));
        return builder;
    }
}
