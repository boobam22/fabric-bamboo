package bamboo.lib.api;

import bamboo.lib.ClientLib;
import bamboo.lib.keybinding.Key;
import bamboo.lib.keybinding.handler.BaseHandler;

public class Client {
    public static void registerKey(String keyString, BaseHandler handler, boolean triggerOnRelease) {
        Key key = Key.parse(keyString);
        if (triggerOnRelease) {
            key.triggerOnRelease();
        }
        key.register(handler);
    }

    public static void registerKey(String keyString, BaseHandler handler) {
        registerKey(keyString, handler, false);
    }

    public static void onJoinWorld(Runnable callback) {
        ClientLib.joinWorldHandlers.register(callback);
    }

    public static void onExitWorld(Runnable callback) {
        ClientLib.exitWorldHandlers.register(callback);
    }

    public static void onExitGame(Runnable callback) {
        ClientLib.exitGameHandlers.register(callback);
    }
}
