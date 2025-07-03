package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.api.Util;
import bamboo.lib.keybinding.handler.IngameHandler;

public class ClientPickaxe extends Pickaxe implements ClientModInitializer {
    public static BreakCooldown breakCooldown = new BreakCooldown();

    @Override
    public void onInitializeClient() {
        Client.registerKey("b+left", switchNextBreakCooldown);
        Client.registerKey("b+right", toggleAreaMine);

        Client.onExitWorld(() -> {
            if (areaMine.isEnabled()) {
                areaMine.toggle();
            }
        });
    }

    private static IngameHandler switchNextBreakCooldown = client -> {
        breakCooldown.switchNext();
        Util.message("Break Cooldown [§a%s§f]", breakCooldown);
    };

    private static IngameHandler toggleAreaMine = client -> {
        boolean enabled = areaMine.toggle();
        Util.message("Area Mine [%s%s§f]", enabled ? "§a" : "§c", enabled);
    };
}
