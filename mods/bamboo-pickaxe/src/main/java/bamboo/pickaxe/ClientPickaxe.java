package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.text.Text;

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
        breakCooldown.switchNext();
        Text text = Text.of(String.format("Break Cooldown [§a%s§f]", breakCooldown));
        client.inGameHud.setOverlayMessage(text, false);
        return true;
    };

    private static IngameHandler toggleAreaMine = client -> {
        boolean enabled = areaMine.toggle();
        Text text = Text.of(String.format("Area Mine [%s%s§f]", enabled ? "§a" : "§c", enabled));
        client.inGameHud.setOverlayMessage(text, false);
        return true;
    };
}
