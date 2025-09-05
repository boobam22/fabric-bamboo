package bamboo.pickaxe;

import bamboo.lib.api.Server;
import bamboo.lib.config.ConfigEntry;

public class BreakCooldown {
    private static ConfigEntry<State> entry = Server.registerConfig("pickaxe.breakCooldown", State.DEFAULT);

    public boolean isDefault() {
        return entry.getValue() == State.DEFAULT;
    }

    public boolean isNever() {
        return entry.getValue() == State.NEVER;
    }

    public boolean isAlways() {
        return entry.getValue() == State.ALWAYS;
    }

    public void toggle() {
        if (entry.getValue() == State.NEVER) {
            entry.set(State.ALWAYS);
        } else {
            entry.set(State.NEVER);
        }
    }

    @Override
    public String toString() {
        return entry.toString();
    }

    private static enum State {
        DEFAULT, NEVER, ALWAYS;
    }
}
