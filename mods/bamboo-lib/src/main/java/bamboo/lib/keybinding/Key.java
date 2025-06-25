package bamboo.lib.keybinding;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

public class Key {
    public int key;
    public Set<Integer> modifier;
    public int action;

    public Key(int key, Set<Integer> modifier, int action) {
        this.key = key;
        this.modifier = modifier;
        this.action = action;
    }

    public Key(int key, Set<Integer> modifier) {
        this(key, modifier, GLFW.GLFW_PRESS);
    }

    public Key triggerOnPress() {
        this.action = GLFW.GLFW_PRESS;
        return this;
    }

    public Key triggerOnRelease() {
        this.action = GLFW.GLFW_RELEASE;
        return this;
    }

    public void execute(Handler callback) {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Key other) {
            if (key == other.key && modifier.equals(other.modifier)) {
                return key == KeyMap.SCROLL || key == KeyMap.MOVE ? true : action == other.action;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, modifier, action);
    }

    @Override
    public String toString() {
        return Stream.concat(modifier.stream(), Stream.of(key))
                .map(KeyMap::toName)
                .collect(Collectors.joining("+"));
    }

    public static Key parse(String key) {
        LinkedHashSet<Integer> keyCodes = Stream.of(key.split("\\+"))
                .map(KeyMap::toCode)
                .filter(code -> code != KeyMap.UNKNOWN)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (keyCodes.size() > 0) {
            return new Key(keyCodes.removeLast(), keyCodes);
        }
        return null;
    }
}
