package bamboo.lib.api;

import java.util.function.Function;

import bamboo.lib.Lib;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.command.Command;

public class Server {
    public static ConfigEntry<String> registerConfig(String key, String value) {
        return Lib.configRegistry.register(key, value, str -> str);
    }

    public static ConfigEntry<Boolean> registerConfig(String key, boolean value) {
        return Lib.configRegistry.register(key, value, Boolean::parseBoolean);
    }

    public static ConfigEntry<Integer> registerConfig(String key, int value) {
        return Lib.configRegistry.register(key, value, Integer::parseInt);
    }

    public static ConfigEntry<Double> registerConfig(String key, double value) {
        return Lib.configRegistry.register(key, value, Double::parseDouble);
    }

    public static <T extends Enum<T>> ConfigEntry<T> registerConfig(String key, T value) {
        return Lib.configRegistry.register(key, value, str -> Enum.valueOf(value.getDeclaringClass(), str));
    }

    public static <T> ConfigEntry<T> registerConfig(String key, T value, Function<String, T> constructor) {
        return Lib.configRegistry.register(key, value, constructor);
    }

    public static void registerCommand(Command cmd) {
        Lib.commandRegistry.register(cmd);
    }
}
