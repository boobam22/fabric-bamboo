package bamboo.lib.config;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

public class ConfigEntry<T> {
    private final String key;
    private final T defaultValue;
    private final Function<String, T> constructor;
    private T value;
    private List<T> presets;

    @SuppressWarnings("unchecked")
    public ConfigEntry(String key, T value, Function<String, T> constructor) {
        this.key = key;
        this.defaultValue = value;
        this.constructor = constructor;
        this.value = value;
        this.presets = new ArrayList<>();

        List<T> list;
        if (value instanceof Boolean) {
            list = (List<T>) List.of(true, false);
        } else if (value instanceof Enum<?>) {
            Class<? extends Enum<?>> enumClass = ((Enum<?>) value).getDeclaringClass();
            list = (List<T>) List.of(enumClass.getEnumConstants());
        } else {
            return;
        }

        int idx = list.indexOf(defaultValue);
        this.presets.addAll(list.subList(idx, list.size()));
        this.presets.addAll(list.subList(0, idx));
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

    public List<T> getPresets() {
        return presets;
    }

    public void set(T value) {
        this.value = value;
    }

    public void reset() {
        this.set(defaultValue);
    }

    public void toggle() {
        if (presets.size() > 1) {
            int idx = presets.indexOf(value);
            this.set(presets.get((idx + 1) % presets.size()));
        }
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
