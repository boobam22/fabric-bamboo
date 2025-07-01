package bamboo.lib;

import net.fabricmc.api.ClientModInitializer;

public class ClientLib extends Lib implements ClientModInitializer {
    public static final Registry<Runnable> joinWorldHandlers = new Registry<>();
    public static final Registry<Runnable> exitWorldHandlers = new Registry<>();
    public static final Registry<Runnable> exitGameHandlers = new Registry<>();

    @Override
    public void onInitializeClient() {
        exitWorldHandlers.register(() -> getConfigRegistry().saveConfig());
    }
}
