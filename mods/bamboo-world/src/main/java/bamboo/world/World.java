package bamboo.world;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.world.command.WorldCommand;

public class World implements ModInitializer {
    @Override
    public void onInitialize() {
        Server.registerCommand(new WorldCommand());
    }
}