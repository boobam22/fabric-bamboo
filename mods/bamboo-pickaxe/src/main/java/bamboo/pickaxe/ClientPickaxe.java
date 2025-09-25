package bamboo.pickaxe;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.keybinding.IngameHandler;

public class ClientPickaxe implements ClientModInitializer {
    public static ConfigEntry<Boolean> breakCooldown = Client.registerConfig("pickaxe.breakCooldown", false);
    public static ConfigEntry<Boolean> disableCorpseRender;
    public static ConfigEntry<Boolean> disableItemRender;
    public static ConfigEntry<Boolean> disableMonsterRender;
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
        Client.message("Break Cooldown [%s]", breakCooldown.getValue());
        return true;
    };

    private static IngameHandler toggleAreaMine = client -> {
        areaMine.toggle();
        Client.message("Area Mine [%s]", areaMine.isEnabled());
        return true;
    };

    static {
        disableCorpseRender = Client.registerConfig("pickaxe.disableCorpseRender", false);
        disableItemRender = Client.registerConfig("pickaxe.disableItemRender", false);
        disableMonsterRender = Client.registerConfig("pickaxe.disableMonsterRender", false);
    }
}
