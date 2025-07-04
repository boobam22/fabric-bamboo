package bamboo.lib;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Registry<T> {
    private final List<T> items = new ArrayList<>();

    public void register(T item) {
        items.add(item);
    }

    public void forEach(Consumer<T> action) {
        items.forEach(action);
    }

    public void runAll() {
        forEach(item -> {
            if (item instanceof Runnable runnable) {
                runnable.run();
            }
        });
    }
}
