package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.keybinding.Key;
import bamboo.lib.lifecycle.MinecraftClientLifecycle;

public class Pickaxe implements ClientModInitializer {
    public static BreakCooldown breakCooldown = new BreakCooldown();
    public static AreaMine areaMine = new AreaMine();

    @Override
    public void onInitializeClient() {
        Key.parse("b+left").execute(breakCooldown::switchNext);
        Key.parse("b+right").execute(areaMine::toggle);

        MinecraftClientLifecycle.onExitWorld(() -> {
            if (areaMine.isEnabled()) {
                areaMine.toggle();
            }
        });
    }
}
