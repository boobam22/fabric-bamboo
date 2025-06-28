package bamboo.lib.command;

import java.util.List;
import java.util.ArrayList;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;

public class CommandManager {
    private static List<AbstractCommand> commands = new ArrayList<>();

    public static void register(AbstractCommand command) {
        commands.add(command);
    }

    public static void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            RegistrationEnvironment env,
            CommandRegistryAccess access) {
        commands.forEach(command -> command.register(dispatcher, env, access));
    }
}
