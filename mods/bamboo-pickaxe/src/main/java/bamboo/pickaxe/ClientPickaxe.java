package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.MinecraftClient;

import bamboo.lib.api.Client;
import bamboo.lib.api.Util;
import bamboo.lib.keybinding.handler.IngameHandler;

public class ClientPickaxe extends Pickaxe implements ClientModInitializer {
    public static BreakCooldown breakCooldown = new BreakCooldown();

    @Override
    public void onInitializeClient() {
        Client.registerKey("b+left", (IngameHandler) ClientPickaxe::switchNextBreakCooldown);
        Client.registerKey("b+right", areaMine::toggle);

        Client.onExitWorld(() -> {
            if (areaMine.isEnabled()) {
                areaMine.toggle();
            }
        });
    }

    public static void switchNextBreakCooldown(MinecraftClient client) {
        breakCooldown.switchNext();
        Util.message("Break Cooldown", breakCooldown.toString());
    }
}
