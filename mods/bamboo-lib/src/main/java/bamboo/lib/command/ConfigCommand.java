package bamboo.lib.command;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import bamboo.lib.Lib;
import bamboo.lib.config.ConfigEntry;

public class ConfigCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-lib")
                .then(literal("config")
                        .then(argument("key", StringArgumentType.string())
                                .suggests(configKeys)
                                .executes(getConfig)
                                .then(argument("value", StringArgumentType.string())
                                        .suggests(configValues)
                                        .executes(setConfig)))));
    }

    private static void showConfigEntry(ServerCommandSource source, ConfigEntry<?> entry) {
        source.sendMessage(Text.of(String.format("%s  default: §a%s§f  current: §a%s§f",
                entry.getKey(), entry.getDefaultValue(), entry.getValue())));
    }

    private static Decorator.Base getConfig = ctx -> {
        String key = StringArgumentType.getString(ctx, "key");
        ConfigEntry<?> entry = Lib.configRegistry.get(key);
        if (entry != null) {
            showConfigEntry(ctx.getSource(), entry);
            return 1;
        }
        return 0;
    };

    private static Decorator.Base setConfig = ctx -> {
        String key = StringArgumentType.getString(ctx, "key");
        String value = StringArgumentType.getString(ctx, "value");
        ConfigEntry<?> entry = Lib.configRegistry.get(key);

        if (entry != null) {
            entry.parse(value);
            showConfigEntry(ctx.getSource(), entry);
            return 1;
        }
        return 0;
    };

    private static Decorator.BaseSuggestion configKeys = ctx -> {
        return Lib.configRegistry.getAll().keySet().stream().toList();
    };

    private static Decorator.BaseSuggestion configValues = ctx -> {
        String key = StringArgumentType.getString(ctx, "key");
        ConfigEntry<?> entry = Lib.configRegistry.get(key);

        if (entry == null) {
            return List.of();
        }

        return entry.getPresets().stream()
                .filter(val -> !val.equals(entry.getValue()))
                .map(e -> e.toString())
                .toList();
    };
}
