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
