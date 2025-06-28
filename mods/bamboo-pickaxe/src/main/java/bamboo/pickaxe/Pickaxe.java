package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.keybinding.Key;

public class Pickaxe implements ClientModInitializer {
    public static BreakCooldown breakCooldown = new BreakCooldown();

    @Override
    public void onInitializeClient() {
        Key.parse("b+left").execute(breakCooldown::switchNext);
    }
}
