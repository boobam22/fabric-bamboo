package bamboo.lib.config;

import java.util.function.Function;

public class ConfigEntry<T> {
    private final String key;
    private final T defaultValue;
    private final Function<String, T> constructor;
    private T value;

    public ConfigEntry(String key, T value, Function<String, T> constructor) {
        this.key = key;
        this.defaultValue = value;
        this.constructor = constructor;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void reset() {
        this.set(defaultValue);
    }

    public void parse(String string) {
        try {
            this.set(constructor.apply(string));
        } catch (Exception e) {
        }
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
