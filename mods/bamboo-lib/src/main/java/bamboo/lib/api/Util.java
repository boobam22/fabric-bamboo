package bamboo.lib.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Util {
    public static void message(String plain, String highlight, boolean isGreen) {
        Text text = Text.literal(plain + " ")
                .append(Text.literal(highlight).formatted(isGreen ? Formatting.GREEN : Formatting.RED));
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(text, false);
    }

    public static void message(String plain, String highlight) {
        message(plain, highlight, true);
    }
}
