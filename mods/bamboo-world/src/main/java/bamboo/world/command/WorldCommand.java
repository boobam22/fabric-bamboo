package bamboo.world.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;

import bamboo.lib.command.SimpleCommand;

public class WorldCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        new ChunkCommand().register(dispatcher);
        new PointCommand().register(dispatcher);
        new TpCommand().register(dispatcher);
        new BackupCommand().register(dispatcher);
    }
}
