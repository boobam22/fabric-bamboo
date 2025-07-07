package bamboo.world.command;

import java.util.List;
import java.util.ArrayList;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.text.Text;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.ColumnPos;
import net.minecraft.util.math.BlockPos;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;
import bamboo.world.World;
import bamboo.world.util.RegionPos;

public class RegionCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("region")
                        .then(argument("dimension", DimensionArgumentType.dimension())
                                .executes(listRegion)
                                .then(literal("add")
                                        .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                                .suggests(currentRegion)
                                                .executes(addRegion)))
                                .then(literal("rm")
                                        .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                                .suggests(addedRegion)
                                                .executes(rmRegion))))));
    }

    private static List<RegionPos> getRegions(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerWorld world = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
        if (World.regions.containsKey(world.getRegistryKey())) {
            return World.regions.get(world.getRegistryKey());
        }

        List<RegionPos> regions = new ArrayList<>();
        World.regions.put(world.getRegistryKey(), regions);
        return regions;
    }

    private static Decorator.Base listRegion = ctx -> {
        String dimension = DimensionArgumentType.getDimensionArgument(ctx, "dimension")
                .getRegistryKey().getValue().toString();
        List<RegionPos> regions = getRegions(ctx);

        ctx.getSource().sendMessage(Text.of(String.format("§a%d§f region in §a%s§f", regions.size(), dimension)));
        regions.forEach(regionPos -> {
            ColumnPos center = regionPos.getCenter();
            String tpCommand = String.format("/execute in %s run bb-world tp %d %d", dimension, center.x(), center.z());
            String rmCommand = String.format("/bb-world region %s rm %d %d", dimension, regionPos.x(), regionPos.z());

            Text tp = Text.literal(String.format("§a%s§f", regionPos)).styled(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(Text.of(tpCommand)))
                    .withClickEvent(new ClickEvent.RunCommand(tpCommand)));
            Text rm = Text.literal(" §c✖§f ").styled(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(Text.of(rmCommand)))
                    .withClickEvent(new ClickEvent.SuggestCommand(rmCommand)));

            ctx.getSource().sendMessage(rm.copy().append(tp));
        });
        return regions.size();
    };

    private static Decorator.Base addRegion = ctx -> {
        List<RegionPos> regions = getRegions(ctx);
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        return regions.add(new RegionPos(pos.x(), pos.z())) ? 1 : 0;
    };

    private static Decorator.Base rmRegion = ctx -> {
        List<RegionPos> regions = getRegions(ctx);
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        return regions.remove(new RegionPos(pos.x(), pos.z())) ? 1 : 0;
    };

    private static Decorator.BaseSuggestion currentRegion = ctx -> {
        BlockPos blockPos = ctx.getSource().getEntity().getBlockPos();
        return List.of(String.format("%d %d", blockPos.getX() >> 9, blockPos.getZ() >> 9));
    };

    private static Decorator.BaseSuggestion addedRegion = ctx -> {
        return getRegions(ctx).stream().map(regionPos -> String.format("%d %d", regionPos.x(), regionPos.z())).toList();
    };
}
