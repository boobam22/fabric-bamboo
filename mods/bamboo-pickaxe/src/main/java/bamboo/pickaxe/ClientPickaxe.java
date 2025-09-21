package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.keybinding.IngameHandler;

public class ClientPickaxe extends Pickaxe implements ClientModInitializer {
    public static BreakCooldown breakCooldown = new BreakCooldown();
    public static AreaMine areaMine = new AreaMine();

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
        breakCooldown.toggle();
        Client.message("Break Cooldown [§a%s§f]", breakCooldown);
        return true;
    };

    private static IngameHandler toggleAreaMine = client -> {
        areaMine.toggle();
        Client.message("Area Mine [%s]", areaMine.isEnabled());
        return true;
    };
}
