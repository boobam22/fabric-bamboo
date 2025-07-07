package bamboo.lib.command;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.suggestion.Suggestions;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class Decorator {
    @FunctionalInterface
    public static interface Base extends Command<ServerCommandSource> {
    }

    @FunctionalInterface
    public static interface Player extends Base {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return run(ctx.getSource().getPlayer());
        }

        int run(ServerPlayerEntity player) throws CommandSyntaxException;
    }

    @FunctionalInterface
    public static interface WithPlayer extends Base {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return run(ctx, ctx.getSource().getPlayer());
        }

        int run(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) throws CommandSyntaxException;
    }

    @FunctionalInterface
    public static interface World extends Base {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return run(ctx.getSource().getWorld());
        }

        int run(ServerWorld world) throws CommandSyntaxException;
    }

    @FunctionalInterface
    public static interface WithWorld extends Base {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return run(ctx, ctx.getSource().getWorld());
        }

        int run(CommandContext<ServerCommandSource> ctx, ServerWorld world) throws CommandSyntaxException;
    }

    @FunctionalInterface
    public interface BaseSuggestion extends SuggestionProvider<ServerCommandSource> {
        @Override
        default CompletableFuture<Suggestions> getSuggestions(
                CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) throws CommandSyntaxException {
            return CommandSource.suggestMatching(getSuggestions(ctx), builder);
        }

        List<String> getSuggestions(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException;
    }

    @FunctionalInterface
    public interface PlayerSuggestion extends BaseSuggestion {
        @Override
        default List<String> getSuggestions(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return getSuggestions(ctx.getSource().getPlayer());
        }

        List<String> getSuggestions(ServerPlayerEntity player) throws CommandSyntaxException;
    }

    @FunctionalInterface
    public interface WithPlayerSuggestion extends BaseSuggestion {
        @Override
        default List<String> getSuggestions(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return getSuggestions(ctx, ctx.getSource().getPlayer());
        }

        List<String> getSuggestions(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player)
                throws CommandSyntaxException;
    }

    @FunctionalInterface
    public interface WorldSuggestion extends BaseSuggestion {
        @Override
        default List<String> getSuggestions(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return getSuggestions(ctx.getSource().getWorld());
        }

        List<String> getSuggestions(ServerWorld world) throws CommandSyntaxException;
    }

    @FunctionalInterface
    public interface WithWorldSuggestion extends BaseSuggestion {
        @Override
        default List<String> getSuggestions(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
            return getSuggestions(ctx, ctx.getSource().getWorld());
        }

        List<String> getSuggestions(CommandContext<ServerCommandSource> ctx, ServerWorld world)
                throws CommandSyntaxException;
    }
}
