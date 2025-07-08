package bamboo.world.command;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;

import net.fabricmc.loader.api.FabricLoader;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.util.WorldSavePath;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;
import bamboo.world.data.RegionManager;

public class BackupCommand implements SimpleCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("bb-world")
                .then(literal("backup").executes(backup)));
    }

    private static Decorator.Base backup = ctx -> {
        MinecraftServer server = ctx.getSource().getServer();

        server.getWorlds().forEach(world -> {
            world.savingDisabled = true;
        });

        server.saveAll(true, true, true);

        Path src = server.getSavePath(WorldSavePath.ROOT).normalize();
        Path dst = FabricLoader.getInstance().getGameDir().resolve("backups").resolve(src.getFileName());

        Path SESSION_LOCK = server.getSavePath(WorldSavePath.SESSION_LOCK).normalize();
        Map<Path, ServerWorld> dimensionPath2World = new HashMap<>();

        server.getWorlds().forEach(world -> {
            dimensionPath2World.put(DimensionType.getSaveDirectory(world.getRegistryKey(), src), world);
        });

        new Thread(() -> {
            try {
                if (Files.exists(dst)) {
                    Files.walkFileTree(dst, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }

                Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
                    private List<ServerWorld> worldStack = new ArrayList<>();

                    private boolean isExcluded(Path file) {
                        if (file.equals(SESSION_LOCK)) {
                            return true;
                        }

                        String filename = file.getFileName().toString();
                        if (filename.endsWith(".mca")) {
                            String[] parts = filename.split("\\.");
                            int x = Integer.parseInt(parts[1]);
                            int z = Integer.parseInt(parts[2]);
                            return !RegionManager.get(worldStack.getLast()).contains(x, z);
                        }

                        return false;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        Files.createDirectories(dst.resolve(src.relativize(dir)));
                        if (dimensionPath2World.containsKey(dir)) {
                            worldStack.addLast(dimensionPath2World.get(dir));
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!isExcluded(file)) {
                            Files.copy(file, dst.resolve(src.relativize(file)));
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                        if (dimensionPath2World.containsKey(dir)) {
                            worldStack.removeLast();
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (Exception e) {
            } finally {
                server.execute(() -> {
                    server.getWorlds().forEach(world -> {
                        world.savingDisabled = false;
                    });
                });
            }
        }).start();

        return 1;
    };
}
