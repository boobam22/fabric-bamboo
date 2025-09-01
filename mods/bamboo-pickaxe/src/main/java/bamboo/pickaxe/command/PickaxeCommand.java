package bamboo.pickaxe.command;

import java.util.Iterator;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Identifier;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;

public class PickaxeCommand implements SimpleCommand {
    private static BlockState AIR;
    private static TagKey<Block> ORE_TAG;
    private static TagKey<Block> WHITELIST_TAG;

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-pickaxe")
                .then(literal("clean")
                        .then(argument("from", BlockPosArgumentType.blockPos())
                                .then(argument("to", BlockPosArgumentType.blockPos())
                                        .executes(cleanArea)))));
    }

    private static void cleanArea(CommandContext<ServerCommandSource> ctx, Iterator<BlockPos> iterator) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        ServerWorld world = ctx.getSource().getWorld();
        int limit = world.getGameRules().getInt(GameRules.COMMAND_MODIFICATION_BLOCK_LIMIT);

        int count = 0;
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (!world.isPosLoaded(pos)) {
                continue;
            }

            if (player.getGameMode() == GameMode.SURVIVAL) {
                BlockState blockState = world.getBlockState(pos);
                if (blockState.isIn(WHITELIST_TAG) || blockState.getHardness(world, pos) < 0) {
                    continue;
                }
                if (blockState.isIn(ORE_TAG) && player.canHarvest(blockState)) {
                    Block.getDroppedStacks(blockState, world, pos, null, null, player.getMainHandStack())
                            .forEach(stack -> {
                                if (!player.getInventory().insertStack(stack)) {
                                    Block.dropStack(world, player.getBlockPos(), stack);
                                }
                            });
                }
            }

            if (world.setBlockState(pos, AIR, 950) && ++count == limit) {
                break;
            }
        }

        if (count == limit) {
            ctx.getSource().getServer().execute(() -> {
                cleanArea(ctx, iterator);
            });
        }
    }

    private static Decorator.Base cleanArea = ctx -> {
        BlockPos from = BlockPosArgumentType.getBlockPos(ctx, "from");
        BlockPos to = BlockPosArgumentType.getBlockPos(ctx, "to");
        Iterator<BlockPos> iterator = BlockPos.iterate(from, to).iterator();

        cleanArea(ctx, iterator);
        return 1;
    };

    static {
        AIR = Blocks.AIR.getDefaultState();

        Identifier ore = Identifier.of("bamboo-pickaxe", "ores");
        Identifier whitelist = Identifier.of("bamboo-pickaxe", "whitelist");
        ORE_TAG = TagKey.of(RegistryKeys.BLOCK, ore);
        WHITELIST_TAG = TagKey.of(RegistryKeys.BLOCK, whitelist);
    }
}
