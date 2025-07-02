package bamboo.inventory.command;

import java.util.List;
import java.util.ArrayList;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.registry.tag.ItemTags;
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
                                .then(slotArgument)))
                .then(CommandManager.literal("refresh-trade")
                        .then(CommandManager.argument("villager", EntityArgumentType.entity())
                                .suggests(findVillager)
                                .executes(refreshTrade))));
    }

    private static Command<ServerCommandSource> openCraftingTable = context -> {
        Util.openCraftingTable(context.getSource().getPlayer());
        return 0;
    };
    private static Command<ServerCommandSource> openEnderChest = context -> {
        Util.openEnderChest(context.getSource().getPlayer());
        return 0;
    };
    private static Command<ServerCommandSource> openShulkerBox = context -> {
        return 0;
    };
    private static Command<ServerCommandSource> refreshTrade = context -> {
        Entity entity = EntityArgumentType.getEntity(context, "villager");
        if (Util.canRefeshTrade(entity)) {
            VillagerEntity villager = (VillagerEntity) entity;
            villager.setOffers(null);

            ServerPlayerEntity player = context.getSource().getPlayer();
            if (player.currentScreenHandler instanceof MerchantScreenHandler) {
                player.sendTradeOffers(player.currentScreenHandler.syncId, villager.getOffers(),
                        villager.getVillagerData().level(), villager.getExperience(),
                        villager.isLeveledMerchant(), villager.canRefreshTrades());
            }
        }
        return 0;
    };

    private static SuggestionProvider<ServerCommandSource> findShulkerBox = (context, builder) -> {
        ServerPlayerEntity player = context.getSource().getPlayer();

        Inventory inventory = null;
        if (context.getNodes().get(2).getNode().getName() == "inventory") {
            inventory = player.getInventory();
        } else if (context.getNodes().get(2).getNode().getName() == "ender-chest") {
            inventory = player.getEnderChestInventory();
        }

        List<String> slots = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isIn(ItemTags.SHULKER_BOXES)) {
                slots.add(String.valueOf(i));
            }
        }
        return CommandSource.suggestMatching(slots, builder);
    };
    private static SuggestionProvider<ServerCommandSource> findVillager = (context, builder) -> {
        Vec3d pos = context.getSource().getPlayer().getPos();
        Box range = new Box(pos, pos).expand(16);
        List<String> villagers = context.getSource().getWorld()
                .getEntitiesByClass(VillagerEntity.class, range, Util::canRefeshTrade)
                .stream().map(e -> e.getUuidAsString()).toList();

        return CommandSource.suggestMatching(villagers, builder);
    };
}
