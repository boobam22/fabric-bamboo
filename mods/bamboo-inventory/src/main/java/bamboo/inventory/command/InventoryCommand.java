package bamboo.inventory.command;

import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

import bamboo.lib.command.Command;
import bamboo.inventory.mixin.MerchantScreenHandlerAccessor;

public class InventoryCommand implements Command {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher,
            RegistrationEnvironment env, CommandRegistryAccess access) {

        RequiredArgumentBuilder<ServerCommandSource, Integer> idx = CommandManager
                .argument("idx", IntegerArgumentType.integer(0, 99));

        dispatcher.register(CommandManager.literal("bb-inventory")
                .then(CommandManager.literal("open-crafting-table")
                        .executes(openCraftingTable))
                .then(CommandManager.literal("open-ender-chest")
                        .executes(openEnderChest))
                .then(CommandManager.literal("open-shulker-box")
                        .then(idx.suggests(findShulkerBoxFromCurrentScreen)
                                .executes(openShulkerBoxFromCurrentScreen))
                        .then(CommandManager.literal("inventory")
                                .then(idx.suggests(findShulkerBoxFromInventory)
                                        .executes(openShulkerBoxFromInventory)))
                        .then(CommandManager.literal("ender-chest")
                                .then(idx.suggests(findShulkerBoxFromEnderChest)
                                        .executes(openShulkerBoxFromEnderChest))))
                .then(CommandManager.literal("refresh-trade")
                        .executes(refreshTradeFromCurrentScreen)
                        .then(CommandManager.argument("uuid", StringArgumentType.string())
                                .suggests(findVillager)
                                .executes(refreshTrade))));
    }

    private static Decorator.WithPlayer openCraftingTable = (ctx, player) -> {
        Util.openCraftingTable(player);
    };

    private static Decorator.WithPlayer openEnderChest = (ctx, player) -> {
        Util.openEnderChest(player);
    };

    private static Decorator.OpenShulkerBox openShulkerBoxFromCurrentScreen = (ctx, player, idx) -> {
        return player.currentScreenHandler.getSlot(idx).getStack();
    };

    private static Decorator.OpenShulkerBox openShulkerBoxFromInventory = (ctx, player, idx) -> {
        return player.getInventory().getStack(idx);
    };

    private static Decorator.OpenShulkerBox openShulkerBoxFromEnderChest = (ctx, player, idx) -> {
        return player.getEnderChestInventory().getStack(idx);
    };

    private static Decorator.WithPlayer refreshTradeFromCurrentScreen = (ctx, player) -> {
        Entity entity = (Entity) ((MerchantScreenHandlerAccessor) player.currentScreenHandler).bamboo_getMerchant();
        Util.refreshTrade(player, entity);
    };

    private static Decorator.WithPlayer refreshTrade = (ctx, player) -> {
        UUID uuid = UUID.fromString(StringArgumentType.getString(ctx, "uuid"));
        Entity entity = player.getWorld().getEntity(uuid);
        Util.refreshTrade(player, entity);
    };

    private static Decorator.ShulkerBoxSuggestions findShulkerBoxFromCurrentScreen = (ctx, player) -> {
        return player.currentScreenHandler.getStacks();
    };

    private static Decorator.ShulkerBoxSuggestions findShulkerBoxFromInventory = (ctx, player) -> {
        return player.getInventory().getMainStacks();
    };

    private static Decorator.ShulkerBoxSuggestions findShulkerBoxFromEnderChest = (ctx, player) -> {
        return player.getEnderChestInventory().getHeldStacks();
    };

    private static Decorator.BaseSuggestions findVillager = (ctx, player) -> {
        Box range = new Box(player.getPos(), player.getPos()).expand(16);
        return player.getWorld()
                .getEntitiesByClass(Entity.class, range, Util::canRefeshTrade).stream()
                .map(e -> e.getUuidAsString()).toList();
    };
}
