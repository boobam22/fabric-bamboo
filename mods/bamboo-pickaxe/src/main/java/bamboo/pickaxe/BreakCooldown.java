package bamboo.pickaxe;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import bamboo.lib.config.ConfigManager;
import bamboo.lib.config.ConfigEntry;

public class BreakCooldown {
    private static ConfigEntry<State> entry = ConfigManager.addEntry("pickaxe.breakCooldown", State.DEFAULT);

    public boolean isDefault() {
        return entry.getValue() == State.DEFAULT;
    }

    public boolean isNever() {
        return entry.getValue() == State.NEVER;
    }

    public boolean isAlways() {
        return entry.getValue() == State.ALWAYS;
    }

    public void switchNext() {
        entry.set(entry.getValue().next());
    }

    public boolean switchNext(MinecraftClient client) {
        if (client.currentScreen != null) {
            return false;
        }
        switchNext();
        Text text = Text.literal("Break Cooldown ")
                .append(Text.literal(entry.getValue().toString()).formatted(Formatting.GREEN));
        client.inGameHud.setOverlayMessage(text, false);
        return true;
    }

    private static enum State {
        DEFAULT, NEVER, ALWAYS;

        public State next() {
            State[] values = State.values();
            int i = (this.ordinal() + 1) % values.length;
            return values[i];
        }
    }
}
