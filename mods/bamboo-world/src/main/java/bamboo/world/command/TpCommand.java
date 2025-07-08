package bamboo.world.command;

import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.entity.Entity;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColumnPos;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;

public class TpCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("tp")
                        .then(literal("random")
                                .executes(toRandom))
                        .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                .executes(toPosition))));
    }

    private static Tp toRandom = ctx -> {
        int x = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        int z = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        return new ColumnPos(x, z);
    };

    private static Tp toPosition = ctx -> {
        return ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
    };

    private static interface Tp extends Decorator.WithWorld {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx, ServerWorld world) {
            ColumnPos pos = getColumnPos(ctx);
            int x = pos.x();
            int z = pos.z();
            int y = world.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z))
                    .sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, x, z);

            Entity entity = ctx.getSource().getEntity();
            entity.teleport(world, x + 0.5, y + 1, z + 0.5, Set.of(), entity.getYaw(), entity.getPitch(), false);
            return 1;
        }

        ColumnPos getColumnPos(CommandContext<ServerCommandSource> ctx);
    }
}