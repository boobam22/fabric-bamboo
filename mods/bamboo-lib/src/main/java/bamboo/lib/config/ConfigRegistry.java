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
    private static final String fileName = "bamboo.properties";
    private static final Properties properties = loadConfig();
    private final Map<String, ConfigEntry<?>> registry = new TreeMap<>();

    public <T> ConfigEntry<T> register(String key, T value, Function<String, T> constructor) {
        ConfigEntry<T> entry = new ConfigEntry<>(key, value, constructor);
        registry.putIfAbsent(key, entry);

        loadEntry(entry);
        return entry;
    }

    private <T> void loadEntry(ConfigEntry<T> entry) {
        String key = entry.getKey();
        if (properties.containsKey(key)) {
            entry.parse(properties.getProperty(key));
        }
    }

    private static Path getFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve(fileName);
    }

    public static Properties loadConfig() {
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(getFilePath()));
        } catch (IOException e) {
        }
        return props;
    }

    public void saveConfig() {
        registry.values().forEach(entry -> {
            properties.setProperty(entry.getKey(), entry.toString());
        });

        try {
            properties.store(Files.newOutputStream(getFilePath()), fileName);
        } catch (IOException e) {
        }
    }
}
