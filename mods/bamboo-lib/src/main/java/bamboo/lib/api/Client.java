package bamboo.lib.api;

import java.util.function.Function;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import bamboo.lib.ClientLib;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.keybinding.Key;
import bamboo.lib.keybinding.Handler;

public class Client {
    public static ConfigEntry<String> registerConfig(String key, String value) {
        return ClientLib.configRegistry.register(key, value, str -> str);
    }

    public static ConfigEntry<Boolean> registerConfig(String key, boolean value) {
        return ClientLib.configRegistry.register(key, value, Boolean::parseBoolean);
    }

    public static ConfigEntry<Integer> registerConfig(String key, int value) {
        return ClientLib.configRegistry.register(key, value, Integer::parseInt);
    }

    public static ConfigEntry<Double> registerConfig(String key, double value) {
        return ClientLib.configRegistry.register(key, value, Double::parseDouble);
    }

    public static <T extends Enum<T>> ConfigEntry<T> registerConfig(String key, T value) {
        return ClientLib.configRegistry.register(key, value, str -> Enum.valueOf(value.getDeclaringClass(), str));
    }

    public static <T> ConfigEntry<T> registerConfig(String key, T value, Function<String, T> constructor) {
        return ClientLib.configRegistry.register(key, value, constructor);
    }

    public static void registerKey(String keyString, Handler handler, boolean triggerOnRelease) {
        Key key = Key.parse(keyString);
        if (triggerOnRelease) {
            key.triggerOnRelease();
        }
        key.register(handler);
    }

    public static void registerKey(String keyString, Handler handler) {
        registerKey(keyString, handler, false);
    }

    public static void onJoinWorld(Runnable callback) {
        ClientLib.joinWorldHandlers.register(callback);
    }

    public static void onExitWorld(Runnable callback) {
        ClientLib.exitWorldHandlers.register(callback);
    }

    public static void onExitGame(Runnable callback) {
        ClientLib.exitGameHandlers.register(callback);
    }

    public static void message(String fmt, Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Boolean arg) {
                args[i] = (arg ? "§a" : "§c") + arg.toString() + "§f";
            }
        }
        Text text = Text.of(String.format(fmt, args));
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(text, false);
    }
}
