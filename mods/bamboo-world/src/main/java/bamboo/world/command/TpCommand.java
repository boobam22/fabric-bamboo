package bamboo.world.command;

import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;

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

    private static void tp(Entity entity, ServerWorld world, int x, int z) {
        int y = world.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z))
                .sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, x, z);
        entity.teleport(world, x + 0.5, y + 1, z + 0.5, Set.of(), entity.getYaw(), entity.getPitch(), false);
    }

    private static Decorator.WithWorld toRandom = (ctx, world) -> {
        int x = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        int z = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        tp(ctx.getSource().getEntity(), world, x, z);
        return 1;
    };

    private static Decorator.WithWorld toPositionOfCurrentDimension = (ctx, world) -> {
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        tp(ctx.getSource().getEntity(), world, pos.x(), pos.z());
        return 1;
    };

    private static Decorator.Base toPosition = ctx -> {
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        ServerWorld world = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
        tp(ctx.getSource().getEntity(), world, pos.x(), pos.z());
        return 1;
    };
}