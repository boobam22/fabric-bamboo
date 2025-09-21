package bamboo.lib;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.command.Command;

public class Lib implements ModInitializer {
    public static final Registry<Command> commandRegistry = new Registry<>();
    public static final Registry<Runnable> startHandlers = new Registry<>();
    public static final Registry<Runnable> shutdownHandlers = new Registry<>();

    @Override
    public void onInitialize() {
    }
}
