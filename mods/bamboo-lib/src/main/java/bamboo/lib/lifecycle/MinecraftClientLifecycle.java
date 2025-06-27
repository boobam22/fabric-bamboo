package bamboo.lib.lifecycle;

import java.util.List;
import java.util.ArrayList;

public class MinecraftClientLifecycle {
    private static List<Runnable> joinWorldHandlers = new ArrayList<>();
    private static List<Runnable> exitWorldHandlers = new ArrayList<>();
    private static List<Runnable> exitGameHandlers = new ArrayList<>();

    public static void onJoinWorld(Runnable handler) {
        joinWorldHandlers.add(handler);
    }

    public static void onJoinWorld() {
        joinWorldHandlers.forEach(handler -> handler.run());
    }

    public static void onExitWorld(Runnable handler) {
        exitWorldHandlers.add(handler);
    }

    public static void onExitWorld() {
        exitWorldHandlers.forEach(handler -> handler.run());
    }

    public static void onExitGame(Runnable handler) {
        exitGameHandlers.add(handler);
    }

    public static void onExitGame() {
        exitGameHandlers.forEach(handler -> handler.run());
    }
}
