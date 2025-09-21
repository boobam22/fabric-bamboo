package bamboo.lib.config;

import java.util.Map;
import java.util.TreeMap;
import java.util.Properties;
import java.util.function.Function;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigRegistry {
    private final String id;
    private final Properties properties = new Properties();
    private final Map<String, ConfigEntry<?>> registry = new TreeMap<>();

    public ConfigRegistry(String id) {
        this.id = id;
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
            entry.parse(properties.getProperty(key));
        }
    }

    private Path getFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve(id);
    }

    public void loadConfig() {
        properties.clear();
        try (InputStream in = Files.newInputStream(getFilePath())) {
            properties.load(in);
        } catch (IOException e) {
        }

        registry.values().forEach(entry -> {
            loadEntry(entry);
        });
    }

    public void saveConfig() {
        registry.values().forEach(entry -> {
            properties.setProperty(entry.getKey(), entry.toString());
        });

        try (OutputStream out = Files.newOutputStream(getFilePath())) {
            properties.store(out, id);
        } catch (IOException e) {
        }
    }
}
