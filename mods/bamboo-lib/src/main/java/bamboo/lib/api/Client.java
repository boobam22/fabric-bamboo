package bamboo.lib.api;

import bamboo.lib.ClientLib;

public class Client {
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
