package bamboo.lib.keybinding;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

import bamboo.lib.keybinding.event.EventUtil;

public class KeyBinding {
    public int key;
    public List<Integer> modifier;
    public int action;

    public KeyBinding(int key, List<Integer> modifier, int action) {
        this.key = key;
        this.modifier = modifier;
        this.action = action;
    }

    public KeyBinding(int key, List<Integer> modifier) {
        this(key, modifier, GLFW.GLFW_PRESS);
    }

    public KeyBinding triggerOnPress() {
        this.action = GLFW.GLFW_PRESS;
        return this;
    }

    public KeyBinding triggerOnRelease() {
        this.action = GLFW.GLFW_RELEASE;
        return this;
    }

    public void execute(Handler callback) {
        EventUtil.register(this, callback);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KeyBinding other) {
            return key == other.key && modifier.equals(other.modifier) && action == other.action;
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

    public static KeyBinding bind(String key) {
        List<Integer> keyCodes = Stream.of(key.split("\\+"))
                .map(KeyMap::toCode)
                .filter(code -> code != KeyMap.UNKNOWN)
                .toList();

        if (keyCodes.size() > 0) {
            return new KeyBinding(keyCodes.getLast(), keyCodes.subList(0, keyCodes.size() - 1));
        }
        return null;
    }
}
