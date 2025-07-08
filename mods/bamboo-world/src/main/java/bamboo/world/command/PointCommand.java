package bamboo.world.command;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.text.Text;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.ColumnPos;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;
import bamboo.world.data.PointManager;
import bamboo.world.data.Point;

public class PointCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("point")
                        .executes(listPoint)
                        .then(literal("add")
                                .then(argument("name", StringArgumentType.string())
                                        .then(argument("columnPos", ColumnPosArgumentType.columnPos())
                                                .executes(addPoint))))
                        .then(literal("rm")
                                .then(argument("name", StringArgumentType.string())
                                        .suggests(addedPoint)
                                        .executes(rmPoint)))));
    }

    private static Decorator.WithWorld listPoint = (ctx, world) -> {
        List<Point> points = PointManager.get(world).getAll();

        ctx.getSource().sendMessage(Text.of(String.format("§a%d§f point", points.size())));
        points.forEach(point -> {
            String tpCommand = String.format("/bb-world tp point %s", point.name());
            String rmCommand = String.format("/bb-world point rm %s", point.name());

            Text tp = Text.literal(String.format("§a%s§f", point.name())).styled(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(Text.of(tpCommand)))
                    .withClickEvent(new ClickEvent.RunCommand(tpCommand)));
            Text rm = Text.literal(" §c✖§f ").styled(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(Text.of(rmCommand)))
                    .withClickEvent(new ClickEvent.SuggestCommand(rmCommand)));

            ctx.getSource().sendMessage(rm.copy().append(tp));
        });
        return points.size();
    };

    private static Decorator.WithWorld addPoint = (ctx, world) -> {
        String name = StringArgumentType.getString(ctx, "name");
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        return PointManager.get(world).add(name, pos.x(), pos.z(), world.getRegistryKey()) ? 1 : 0;
    };

    private static Decorator.WithWorld rmPoint = (ctx, world) -> {
        String name = StringArgumentType.getString(ctx, "name");
        return PointManager.get(world).remove(name) ? 1 : 0;
    };

    private static Decorator.WorldSuggestion addedPoint = world -> {
        return PointManager.get(world).getAll().stream().map(point -> point.name()).toList();
    };
}
