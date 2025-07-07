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
import bamboo.world.World;
import bamboo.world.util.Point;

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

    private static Decorator.Base listPoint = ctx -> {
        ctx.getSource().sendMessage(Text.of(String.format("§a%d§f point", World.points.size())));
        World.points.keySet().forEach(name -> {
            String tpCommand = String.format("/bb-world tp point %s", name);
            String rmCommand = String.format("/bb-world point rm %s", name);

            Text tp = Text.literal(String.format("§a%s§f", name)).styled(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(Text.of(tpCommand)))
                    .withClickEvent(new ClickEvent.RunCommand(tpCommand)));
            Text rm = Text.literal(" §c✖§f ").styled(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(Text.of(rmCommand)))
                    .withClickEvent(new ClickEvent.SuggestCommand(rmCommand)));

            ctx.getSource().sendMessage(rm.copy().append(tp));
        });
        return World.points.size();
    };

    private static Decorator.WithWorld addPoint = (ctx, world) -> {
        String name = StringArgumentType.getString(ctx, "name");
        ColumnPos pos = ColumnPosArgumentType.getColumnPos(ctx, "columnPos");
        Point point = new Point(name, pos, world.getRegistryKey());
        return World.points.put(name, point) == null ? 1 : 0;
    };

    private static Decorator.Base rmPoint = ctx -> {
        String name = StringArgumentType.getString(ctx, "name");
        return World.points.remove(name) != null ? 1 : 0;
    };

    private static Decorator.BaseSuggestion addedPoint = ctx -> {
        return List.copyOf(World.points.keySet());
    };
}
