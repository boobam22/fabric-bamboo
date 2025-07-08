package bamboo.world;

import java.util.Map;
import java.util.TreeMap;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.world.command.WorldCommand;
import bamboo.world.util.Point;

public class World implements ModInitializer {
    public static Map<String, Point> points = new TreeMap<>();

    @Override
    public void onInitialize() {
        Server.registerCommand(new WorldCommand());
    }
}