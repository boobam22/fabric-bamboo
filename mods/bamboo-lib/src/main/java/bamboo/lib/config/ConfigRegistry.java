package bamboo.lib.config;

import java.util.Map;
import java.util.TreeMap;
import java.util.Properties;
import java.util.function.Function;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigRegistry {
    private static String fileName = "bamboo.properties";
    private Properties properties = new Properties();
    private Map<String, ConfigEntry<?>> registry = new TreeMap<>();

    public ConfigRegistry() {
        loadConfig();
    }

    public <T> ConfigEntry<T> register(String key, T value, Function<String, T> constructor) {
        ConfigEntry<T> entry = new ConfigEntry<>(key, value, constructor);
        registry.putIfAbsent(key, entry);

        loadEntry(entry);
        return entry;
    }

    private <T> void loadEntry(ConfigEntry<T> entry) {
        String key = entry.getKey();
        if (properties.containsKey(key)) {
            entry.set(entry.getConstructor().apply(properties.getProperty(key)));
        }
    }

    public ConfigEntry<String> register(String key, String value) {
        return register(key, value, str -> str);
    }

    public ConfigEntry<Boolean> register(String key, boolean value) {
        return register(key, value, Boolean::parseBoolean);
    }

    public ConfigEntry<Integer> register(String key, int value) {
        return register(key, value, Integer::parseInt);
    }

    public ConfigEntry<Double> register(String key, double value) {
        return register(key, value, Double::parseDouble);
    }

    public <T extends Enum<T>> ConfigEntry<T> register(String key, T value) {
        return register(key, value, str -> Enum.valueOf(value.getDeclaringClass(), str));
    }

    private Path getFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve(fileName);
    }

    public void loadConfig() {
        try {
            properties.load(Files.newInputStream(getFilePath()));
        } catch (IOException e) {
        }

        registry.values().forEach(entry -> loadEntry(entry));
    }

    public void saveConfig() {
        registry.values().forEach(entry -> {
            properties.setProperty(entry.getKey(), entry.getValue().toString());
        });

        try {
            properties.store(Files.newOutputStream(getFilePath()), fileName);
        } catch (IOException e) {
        }
    }
}
