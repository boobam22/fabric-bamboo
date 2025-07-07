package bamboo.world.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;

import bamboo.lib.command.SimpleCommand;

public class BackupCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("backup")));
    }
}
