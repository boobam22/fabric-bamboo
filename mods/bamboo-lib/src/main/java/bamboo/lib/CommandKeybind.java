package bamboo.lib;

import java.util.HashMap;

import bamboo.lib.keybinding.Key;

public class CommandKeybind extends HashMap<Key, String> {
    public static CommandKeybind from(String packed) {
        CommandKeybind instance = new CommandKeybind();
        for (String item : packed.split(";")) {
            String[] kv = item.split(":");
            instance.put(kv[0], kv[1]);
        }
        return instance;
    }

    public String put(String key, String value) {
        return this.put(Key.parse(key), value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.forEach((key, value) -> {
            sb.append(key);
            sb.append(":");
            sb.append(value);
            sb.append(";");
        });
        return sb.toString();
    }
}
