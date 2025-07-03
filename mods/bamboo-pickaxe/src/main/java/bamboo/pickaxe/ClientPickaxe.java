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
        Client.registerKey("b+right", (IngameHandler) ClientPickaxe::toggleAreaMine);

        Client.onExitWorld(() -> {
            if (areaMine.isEnabled()) {
                areaMine.toggle();
            }
        });
    }

    public static void switchNextBreakCooldown(MinecraftClient client) {
        breakCooldown.switchNext();
        Util.message("Break Cooldown [§a%s§f]", breakCooldown);
    }

    public static void toggleAreaMine(MinecraftClient client) {
        boolean enabled = areaMine.toggle();
        Util.message("Area Mine [%s%s§f]", enabled ? "§a" : "§c", enabled);
    }
}
