package bamboo.pickaxe.command;

import java.util.Iterator;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;

public class PickaxeCommand implements SimpleCommand {
    private static BlockState AIR = Blocks.AIR.getDefaultState();

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-pickaxe")
                .then(literal("clean")
                        .then(argument("from", BlockPosArgumentType.blockPos())
                                .then(argument("to", BlockPosArgumentType.blockPos())
                                        .executes(cleanArea)))));
    }

    private static Decorator.WithPlayer cleanArea = (ctx, player) -> {
        BlockPos from = BlockPosArgumentType.getBlockPos(ctx, "from");
        BlockPos to = BlockPosArgumentType.getBlockPos(ctx, "to");

        int count = 0;
        ServerWorld world = player.getServerWorld();
        int limit = world.getGameRules().getInt(GameRules.COMMAND_MODIFICATION_BLOCK_LIMIT);
        Iterator<BlockPos> iterator = BlockPos.iterate(from, to).iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (world.isPosLoaded(pos) && world.setBlockState(pos, AIR, 950) && ++count == limit) {
                break;
            }
        }

        ctx.getSource().sendMessage(Text.of(String.format("§a%d§f blocks changed", count)));
        return count;
    };
}
