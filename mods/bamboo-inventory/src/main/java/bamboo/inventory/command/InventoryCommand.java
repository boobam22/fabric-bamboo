package bamboo.inventory.command;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;
import bamboo.inventory.mixin.MerchantScreenHandlerAccessor;

public class InventoryCommand implements SimpleCommand {
    private static final SimpleCommandExceptionType SHULKER_BOX_NOT_FOUND;
    private static final SimpleCommandExceptionType VILLAGER_NOT_FOUND;

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        RequiredArgumentBuilder<ServerCommandSource, Integer> idx = argument("idx", IntegerArgumentType.integer(0, 99));

        dispatcher.register(literal("bb-inventory")
                .then(literal("open-crafting-table").executes(openCraftingTable))
                .then(literal("open-ender-chest").executes(openEnderChest))
                .then(literal("open-shulker-box")
                        .then(idx.suggests(findShulkerBoxFromCurrentScreen)
                                .executes(openShulkerBoxFromCurrentScreen))
                        .then(literal("inventory")
                                .then(idx.suggests(findShulkerBoxFromInventory)
                                        .executes(openShulkerBoxFromInventory)))
                        .then(literal("ender-chest")
                                .then(idx.suggests(findShulkerBoxFromEnderChest)
                                        .executes(openShulkerBoxFromEnderChest))))
                .then(literal("refresh-trade").executes(refreshTradeFromCurrentScreen)
                        .then(argument("uuid", StringArgumentType.string())
                                .suggests(findVillager).executes(refreshTrade))));
    }

    private static Decorator.Player openCraftingTable = player -> {
        Util.openCraftingTable(player);
        return 0;
    };

    private static Decorator.Player openEnderChest = player -> {
        Util.openEnderChest(player);
        return 0;
    };

    private static OpenShulkerBox openShulkerBoxFromCurrentScreen = (player, idx) -> {
        return player.currentScreenHandler.getSlot(idx);
    };

    private static OpenShulkerBox openShulkerBoxFromInventory = (player, idx) -> {
        return new Slot(player.getInventory(), idx, 0, 0);
    };

    private static OpenShulkerBox openShulkerBoxFromEnderChest = (player, idx) -> {
        return new Slot(player.getEnderChestInventory(), idx, 0, 0);
    };

    private static RefreshTrade refreshTradeFromCurrentScreen = (ctx, player) -> {
        return (Entity) ((MerchantScreenHandlerAccessor) player.currentScreenHandler).bamboo_getMerchant();
    };

    private static RefreshTrade refreshTrade = (ctx, player) -> {
        UUID uuid = UUID.fromString(StringArgumentType.getString(ctx, "uuid"));
        return player.getWorld().getEntity(uuid);
    };

    private static ShulkerBoxSuggestion findShulkerBoxFromCurrentScreen = player -> {
        return player.currentScreenHandler.getStacks();
    };

    private static ShulkerBoxSuggestion findShulkerBoxFromInventory = player -> {
        return player.getInventory().getMainStacks();
    };

    private static ShulkerBoxSuggestion findShulkerBoxFromEnderChest = player -> {
        return player.getEnderChestInventory().getHeldStacks();
    };

    private static Decorator.PlayerSuggestion findVillager = player -> {
        Box range = new Box(player.getPos(), player.getPos()).expand(16);
        return player.getWorld()
                .getEntitiesByClass(Entity.class, range, Util::canRefeshTrade).stream()
                .map(e -> e.getUuidAsString()).toList();
    };

    private static interface OpenShulkerBox extends Decorator.WithPlayer {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player)
                throws CommandSyntaxException {
            int idx = IntegerArgumentType.getInteger(ctx, "idx");
            Slot slot = findSlot(player, idx);
            if (Util.isShulkerBox(slot.getStack())) {
                Util.openShulkerBox(player, slot);
            } else {
                throw SHULKER_BOX_NOT_FOUND.create();
            }
            return 0;
        }

        Slot findSlot(ServerPlayerEntity player, int idx);
    }

    private static interface RefreshTrade extends Decorator.WithPlayer {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player)
                throws CommandSyntaxException {
            Entity entity = getVillager(ctx, player);
            if (Util.canRefeshTrade(entity)) {
                Util.refreshTrade(player, entity);
            } else {
                throw VILLAGER_NOT_FOUND.create();
            }
            return 0;
        }

        Entity getVillager(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player);
    }

    private static interface ShulkerBoxSuggestion extends Decorator.PlayerSuggestion {
        @Override
        default List<String> getSuggestions(ServerPlayerEntity player) {
            List<String> candidates = new ArrayList<>();
            List<ItemStack> stacks = findStacks(player);

            for (int i = 0; i < stacks.size(); i++) {
                if (Util.isShulkerBox(stacks.get(i))) {
                    candidates.add(String.valueOf(i));
                }
            }
            return candidates;
        }

        List<ItemStack> findStacks(ServerPlayerEntity player);
    }

    static {
        SHULKER_BOX_NOT_FOUND = new SimpleCommandExceptionType(Text.of("SHULKER_BOX_NOT_FOUND"));
        VILLAGER_NOT_FOUND = new SimpleCommandExceptionType(Text.of("VILLAGER_NOT_FOUND"));
    }
}
