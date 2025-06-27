package bamboo.pickaxe.config;

import bamboo.lib.config.ConfigManager;
import bamboo.lib.config.ConfigEntry;

public class Config {
    public static ConfigEntry<BreakCooldown> breakCooldown;

    static {
        breakCooldown = ConfigManager.addEntry("pickaxe.breakCooldown", BreakCooldown.DEFAULT, BreakCooldown::valueOf);
    }
}
