package bamboo.lib.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;

@FunctionalInterface
public interface Command {
    void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            RegistrationEnvironment env,
            CommandRegistryAccess access);
}
