package bamboo.inventory.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;

public class InventoryCommand implements bamboo.lib.command.Command {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher,
            RegistrationEnvironment env, CommandRegistryAccess access) {
        RequiredArgumentBuilder<ServerCommandSource, Integer> slotArgument = CommandManager
                .argument("slot", IntegerArgumentType.integer())
                .suggests(findShulkerBox)
                .executes(openShulkerBox);

        dispatcher.register(CommandManager.literal("bb-inventory")
                .then(CommandManager.literal("open-crafting-table")
                        .executes(openCraftingTable))
                .then(CommandManager.literal("open-ender-chest")
                        .executes(openEnderChest))
                .then(CommandManager.literal("open-shulker-box")
                        .then(CommandManager.literal("inventory")
                                .then(slotArgument))
                        .then(CommandManager.literal("ender-chest")
                                .then(slotArgument))
                        .then(CommandManager.literal("block")
                                .then(CommandManager.argument("blockPos", BlockPosArgumentType.blockPos())
                                        .suggests(findChestBlock)
                                        .then(slotArgument))))
                .then(CommandManager.literal("refresh-trade")
                        .then(CommandManager.argument("villager", EntityArgumentType.entity())
                                .suggests(findVillager)
                                .executes(refreshTrade))));
    }

    private static Command<ServerCommandSource> openCraftingTable = context -> {
        return 0;
    };
    private static Command<ServerCommandSource> openEnderChest = context -> {
        return 0;
    };
    private static Command<ServerCommandSource> openShulkerBox = context -> {
        return 0;
    };
    private static Command<ServerCommandSource> refreshTrade = context -> {
        return 0;
    };

    private static SuggestionProvider<ServerCommandSource> findShulkerBox = (context, builder) -> {
        return builder.buildFuture();
    };
    private static SuggestionProvider<ServerCommandSource> findChestBlock = (context, builder) -> {
        return builder.buildFuture();
    };
    private static SuggestionProvider<ServerCommandSource> findVillager = (context, builder) -> {
        return builder.buildFuture();
    };
}
