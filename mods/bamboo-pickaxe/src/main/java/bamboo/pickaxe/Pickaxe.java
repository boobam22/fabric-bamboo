package bamboo.pickaxe;

import net.fabricmc.api.ModInitializer;

import bamboo.lib.api.Server;
import bamboo.pickaxe.command.PickaxeCommand;

public class Pickaxe implements ModInitializer {
    public static AreaMine areaMine = new AreaMine();

    @Override
    public void onInitialize() {
        Server.registerCommand(new PickaxeCommand());
    }
}
