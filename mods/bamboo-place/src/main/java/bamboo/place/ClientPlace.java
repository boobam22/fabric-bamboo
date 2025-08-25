package bamboo.place;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.text.Text;

import bamboo.lib.api.Server;
import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigEntry;
import bamboo.lib.keybinding.IngameHandler;

public class ClientPlace implements ClientModInitializer {
    public static ConfigEntry<Boolean> fastUse = Server.registerConfig("place.fastUse", false);

    @Override
    public void onInitializeClient() {
        Client.registerKey("b+p", toggleFastUse);
    }

    private static IngameHandler toggleFastUse = client -> {
        fastUse.set(!fastUse.getValue());
        Text text = Text.of(String.format("Fast Use [§a%s§f]", fastUse.getValue()));
        client.inGameHud.setOverlayMessage(text, false);
        return true;
    };
}
