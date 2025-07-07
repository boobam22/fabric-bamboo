package bamboo.world.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;

import bamboo.lib.command.SimpleCommand;

public class ChunkCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("chunk")));
    }
}
