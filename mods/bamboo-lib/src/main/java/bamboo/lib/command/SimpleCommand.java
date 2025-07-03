package bamboo.lib.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;

@FunctionalInterface
public interface SimpleCommand extends Command {
    @Override
    default void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            RegistrationEnvironment env,
            CommandRegistryAccess access) {
        register(dispatcher);
    }

    void register(CommandDispatcher<ServerCommandSource> dispatcher);
}
