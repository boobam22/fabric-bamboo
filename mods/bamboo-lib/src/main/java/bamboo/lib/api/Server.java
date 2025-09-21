package bamboo.lib.api;

import bamboo.lib.Lib;
import bamboo.lib.command.Command;

public class Server {
    public static void registerCommand(Command cmd) {
        Lib.commandRegistry.register(cmd);
    }

    public static void onStart(Runnable callback) {
        Lib.startHandlers.register(callback);
    }

    public static void onShutdown(Runnable callback) {
        Lib.shutdownHandlers.register(callback);
    }
}
