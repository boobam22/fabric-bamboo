package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import bamboo.lib.keybinding.Key;
import bamboo.pickaxe.config.Config;
import bamboo.pickaxe.config.BreakCooldown;

public class Pickaxe implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Key.parse("b+left").execute(client -> {
            BreakCooldown oldValue = Config.breakCooldown.getValue();
            BreakCooldown newValue = oldValue.next();
            Config.breakCooldown.set(newValue);

            Text text = Text.literal("Break Cooldown ")
                    .append(Text.literal(newValue.toString()).formatted(Formatting.GREEN));
            client.inGameHud.setOverlayMessage(text, false);
            return true;
        });
    }
}
