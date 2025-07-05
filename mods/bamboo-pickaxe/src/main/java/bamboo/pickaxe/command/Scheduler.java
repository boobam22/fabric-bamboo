package bamboo.pickaxe.command;

import java.util.Map;
import java.util.HashMap;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.command.ServerCommandSource;

public class Scheduler extends MarkerEntity {
    private static Map<String, Scheduler> running = new HashMap<>();
    private ServerCommandSource source;
    private String cmd;

    Scheduler(ServerPlayerEntity player, String cmd) {
        super(EntityType.MARKER, player.getServerWorld());
        this.setPosition(player.getPos());
        this.source = player.getServer().getCommandSource().withEntity(player);
        this.cmd = cmd;
    }

    @Override
    public void tick() {
        boolean shouldKill;
        try {
            shouldKill = this.source.getServer().getCommandManager().getDispatcher().execute(cmd, source) == 0;
        } catch (CommandSyntaxException e) {
            shouldKill = true;
        }

        if (shouldKill) {
            running.remove(cmd);
            this.kill((ServerWorld) this.getWorld());
        }
    }

    public static Scheduler create(ServerPlayerEntity player, String cmd) {
        if (running.containsKey(cmd)) {
            return running.get(cmd);
        }

        Scheduler scheduler = new Scheduler(player, cmd);
        running.put(cmd, scheduler);
        player.getServerWorld().spawnEntity(scheduler);
        return scheduler;
    }
}
