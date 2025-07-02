package bamboo.inventory.command;

import java.util.List;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

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
        VillagerEntity villager = (VillagerEntity) EntityArgumentType.getEntity(context, "villager");
        if (villager.getExperience() == 0
                && !villager.getVillagerData().profession().matchesKey(VillagerProfession.NONE)) {
            villager.setOffers(null);
        }
        return 0;
    };

    private static SuggestionProvider<ServerCommandSource> findShulkerBox = (context, builder) -> {
        return builder.buildFuture();
    };
    private static SuggestionProvider<ServerCommandSource> findChestBlock = (context, builder) -> {
        return builder.buildFuture();
    };
    private static SuggestionProvider<ServerCommandSource> findVillager = (context, builder) -> {
        Vec3d pos = context.getSource().getPlayer().getPos();
        Box range = new Box(pos, pos).expand(16);
        List<String> villagers = context.getSource().getWorld()
                .getEntitiesByClass(VillagerEntity.class, range, villager -> {
                    return !villager.getVillagerData().profession().matchesKey(VillagerProfession.NONE)
                            && villager.getExperience() == 0;
                }).stream().map(e -> e.getUuidAsString()).toList();

        return CommandSource.suggestMatching(villagers, builder);
    };
}
