package bamboo.world.command;

import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.entity.Entity;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColumnPos;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;
import bamboo.world.data.Point;

public class TpCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("tp")
                        .then(literal("random")
                                .executes(toRandom))
                        .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                .executes(toPositionOfCurrentDimension)
                                .then(literal("in")
                                        .then(argument("dimension", DimensionArgumentType.dimension())
                                                .executes(toPosition))))));
    }

    private static Tp toRandom = (ctx, world) -> {
        int x = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        int z = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        return new Point("tmp", x, z, world.getRegistryKey());
    };

    private static Tp toPositionOfCurrentDimension = (ctx, world) -> {
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        return new Point("tmp", pos.x(), pos.z(), world.getRegistryKey());
    };

    private static Tp toPosition = (ctx, world) -> {
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        world = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
        return new Point("tmp", pos.x(), pos.z(), world.getRegistryKey());
    };

    private static interface Tp extends Decorator.WithWorld {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx, ServerWorld world) throws CommandSyntaxException {
            Point point = getColumnPos(ctx, world);
            if (point == null) {
                return 0;
            }

            world = ctx.getSource().getServer().getWorld(point.worldKey());
            int x = point.x();
            int z = point.z();
            int y = world.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z))
                    .sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, x, z);

            Entity entity = ctx.getSource().getEntity();
            entity.teleport(world, x + 0.5, y + 1, z + 0.5, Set.of(), entity.getYaw(), entity.getPitch(), false);
            return 1;
        }

        Point getColumnPos(CommandContext<ServerCommandSource> ctx, ServerWorld world) throws CommandSyntaxException;
    }
}