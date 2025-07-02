package bamboo.inventory.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;

public class Util {
    public static boolean canRefeshTrade(Entity entity) {
        if (entity instanceof VillagerEntity villager) {
            return villager.getExperience() == 0
                    && !villager.getVillagerData().profession().matchesKey(VillagerProfession.NONE);
        }
        return false;
    }
}
