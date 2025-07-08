package bamboo.world.command;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.text.Text;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.ColumnPos;
import net.minecraft.util.math.BlockPos;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;
import bamboo.world.data.RegionManager;
import bamboo.world.data.RegionPos;

public class RegionCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("region")
                        .executes(listRegion)
                        .then(literal("add")
                                .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                        .suggests(currentRegion)
                                        .executes(addRegion)))
                        .then(literal("rm")
                                .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                        .suggests(addedRegion)
                                        .executes(rmRegion)))));
    }

    private static Decorator.WithWorld listRegion = (ctx, world) -> {
        List<RegionPos> regions = RegionManager.get(world).getAll();

        ctx.getSource().sendMessage(Text.of(String.format("§a%d§f region", regions.size())));
        regions.forEach(regionPos -> {
            ColumnPos center = regionPos.getCenter();
            String tpCommand = String.format("/bb-world tp %d %d", center.x(), center.z());
            String rmCommand = String.format("/bb-world region rm %d %d", regionPos.x(), regionPos.z());

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

    private static Decorator.WithWorld addRegion = (ctx, world) -> {
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        return RegionManager.get(world).add(pos.x(), pos.z()) ? 1 : 0;
    };

    private static Decorator.WithWorld rmRegion = (ctx, world) -> {
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        return RegionManager.get(world).remove(pos.x(), pos.z()) ? 1 : 0;
    };

    private static Decorator.BaseSuggestion currentRegion = ctx -> {
        BlockPos blockPos = ctx.getSource().getEntity().getBlockPos();
        return List.of(String.format("%d %d", blockPos.getX() >> 9, blockPos.getZ() >> 9));
    };

    private static Decorator.WorldSuggestion addedRegion = world -> {
        return RegionManager.get(world).getAll().stream()
                .map(regionPos -> String.format("%d %d", regionPos.x(), regionPos.z()))
                .toList();
    };
}
