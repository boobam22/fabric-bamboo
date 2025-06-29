package bamboo.lib.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;

public abstract class AbstractCommand {
    public abstract void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            RegistrationEnvironment env,
            CommandRegistryAccess access);
}
