package bamboo.lib.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Util {
    public static void message(String format, Object... args) {
        String s = String.format(format, args);
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.of(s), false);
    }
}
