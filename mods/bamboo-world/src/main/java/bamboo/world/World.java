package bamboo.world;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.RegistryKey;

import bamboo.lib.api.Server;
import bamboo.world.command.WorldCommand;
import bamboo.world.util.RegionPos;
import bamboo.world.util.Point;

public class World implements ModInitializer {
    public static Map<RegistryKey<net.minecraft.world.World>, List<RegionPos>> regions = new HashMap<>();
    public static Map<String, Point> points = new TreeMap<>();

    @Override
    public void onInitialize() {
        Server.registerCommand(new WorldCommand());
    }
}