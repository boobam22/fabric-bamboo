package bamboo.lib.mixin.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.command.FunctionCommand;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(FunctionCommand.class)
public abstract class FunctionCommandMixin {
    @ModifyArg(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;", remap = false))
    private static LiteralArgumentBuilder<ServerCommandSource> register(
            LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.requires(source -> source.hasPermissionLevel(0));
        return builder;
    }
}
