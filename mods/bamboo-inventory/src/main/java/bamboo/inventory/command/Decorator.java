package bamboo.inventory.command;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.suggestion.Suggestions;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public class Decorator {
    public static interface WithPlayer extends Command<ServerCommandSource> {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) {
            withPlayer(ctx, ctx.getSource().getPlayer());
            return SINGLE_SUCCESS;
        }

        void withPlayer(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player);
    }

    public static interface OpenShulkerBox extends WithPlayer {
        @Override
        default void withPlayer(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {
            int idx = IntegerArgumentType.getInteger(ctx, "idx");
            ItemStack stack = findStack(ctx, player, idx);
            if (stack != null && stack.isIn(ItemTags.SHULKER_BOXES)) {
                Util.openShulkerBox(player, stack);
            }
        }

        ItemStack findStack(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player, int idx);
    }

    public static interface BaseSuggestions extends SuggestionProvider<ServerCommandSource> {
        @Override
        default CompletableFuture<Suggestions> getSuggestions(
                CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(findMatching(ctx, ctx.getSource().getPlayer()), builder);
        }

        List<String> findMatching(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player);
    }

    public static interface ShulkerBoxSuggestions extends BaseSuggestions {
        @Override
        default List<String> findMatching(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {
            List<String> candidates = new ArrayList<>();
            List<ItemStack> stacks = findStacks(ctx, player);

            for (int i = 0; i < stacks.size(); i++) {
                if (stacks.get(i).isIn(ItemTags.SHULKER_BOXES)) {
                    candidates.add(String.valueOf(i));
                }
            }
            return candidates;
        }

        List<ItemStack> findStacks(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player);
    }
}
