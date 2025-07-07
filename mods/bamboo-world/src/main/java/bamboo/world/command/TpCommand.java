package bamboo.world.command;

import java.util.List;
import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.entity.Entity;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColumnPos;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;
import bamboo.world.World;
import bamboo.world.util.Point;

public class TpCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("tp")
                        .then(literal("random")
                                .executes(toRandom))
                        .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                .executes(toPosition))
                        .then(literal("point")
                                .then(argument("name", StringArgumentType.string())
                                        .suggests(addedPoint)
                                        .executes(toPoint)))));
    }

    private static Tp toRandom = ctx -> {
        int x = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        int z = (int) (Math.random() * ServerWorld.HORIZONTAL_LIMIT * 2) - ServerWorld.HORIZONTAL_LIMIT;
        return new Point("tmp", x, z, ctx.getSource().getWorld().getRegistryKey());
    };

    private static Tp toPosition = ctx -> {
        ColumnPos columnPos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        return new Point("tmp", columnPos, ctx.getSource().getWorld().getRegistryKey());
    };

    private static Tp toPoint = ctx -> {
        String name = StringArgumentType.getString(ctx, "name");
        return World.points.getOrDefault(name, null);
    };

    private static interface Tp extends Decorator.Base {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) {
            Point point = getColumnPos(ctx);
            if (point == null) {
                return 0;
            }

            ServerWorld world = ctx.getSource().getServer().getWorld(point.worldKey());
            int x = point.x();
            int z = point.z();
            int y = world.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z))
                    .sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, x, z);

            Entity entity = ctx.getSource().getEntity();
            entity.teleport(world, x + 0.5, y + 1, z + 0.5, Set.of(), entity.getYaw(), entity.getPitch(), false);
            return 1;
        }

        Point getColumnPos(CommandContext<ServerCommandSource> ctx);
    }

    private static Decorator.BaseSuggestion addedPoint = ctx -> {
        return List.copyOf(World.points.keySet());
    };
}