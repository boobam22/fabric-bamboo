package bamboo.lib.mixin.command;

import com.mojang.brigadier.CommandDispatcher;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;

import bamboo.lib.Lib;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
    @Shadow
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V", remap = false))
    private void init(RegistrationEnvironment env, CommandRegistryAccess access, CallbackInfo ci) {
        Lib.commandRegistry.forEach(cmd -> cmd.register(dispatcher, env, access));
    }
}
