package bamboo.render;

import net.fabricmc.api.ClientModInitializer;

import bamboo.lib.api.Client;
import bamboo.lib.config.ConfigEntry;

public class ClientRender implements ClientModInitializer {
    public static ConfigEntry<Boolean> disableCorpse = Client.registerConfig("render.disableCorpse", false);
    public static ConfigEntry<Boolean> disableItem = Client.registerConfig("render.disableItem", false);
    public static ConfigEntry<Boolean> disableMonster = Client.registerConfig("render.disableMonster", false);
    public static ConfigEntry<Boolean> disableParticle = Client.registerConfig("render.disableParticle", false);

    @Override
    public void onInitializeClient() {
    }
}
