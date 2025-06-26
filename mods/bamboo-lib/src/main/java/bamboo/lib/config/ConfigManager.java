package bamboo.lib.config;

import java.util.Set;
import java.util.TreeSet;
import java.util.Properties;
import java.util.function.Function;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigManager {
    private static final String fileName = "bamboo.properties";
    private static final Properties properties = new Properties();
    private static final Set<ConfigEntry<?>> registry = new TreeSet<>();

    private static <T> void setValue(ConfigEntry<T> entry) {
        String key = entry.getKey();
        if (properties.containsKey(key)) {
            entry.set(entry.getConstructor().apply(properties.getProperty(key)));
        }
    }

    public static <T> ConfigEntry<T> addEntry(String key, T value, Function<String, T> constructor) {
        ConfigEntry<T> entry = new ConfigEntry<>(key, value, constructor);
        setValue(entry);
        registry.add(entry);
        return entry;
    }

    public static ConfigEntry<String> addEntry(String key, String value) {
        return addEntry(key, value, str -> str);
    }

    public static ConfigEntry<Boolean> addEntry(String key, boolean value) {
        return addEntry(key, value, Boolean::parseBoolean);
    }

    public static ConfigEntry<Integer> addEntry(String key, Integer value) {
        return addEntry(key, value, Integer::parseInt);
    }

    public static ConfigEntry<Double> addEntry(String key, Double value) {
        return addEntry(key, value, Double::parseDouble);
    }

    public static void loadConfig() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(fileName);
        try {
            properties.load(Files.newInputStream(path));
        } catch (IOException e) {
        }

        registry.forEach(entry -> setValue(entry));
    }

    public static void saveConfig() {
        Properties props = new Properties();
        registry.forEach(entry -> {
            props.setProperty(entry.getKey(), entry.getValue().toString());
        });

        Path path = FabricLoader.getInstance().getConfigDir().resolve(fileName);
        try {
            properties.store(Files.newOutputStream(path), fileName);
        } catch (IOException e) {
        }
    }
}
