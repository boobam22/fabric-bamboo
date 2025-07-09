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

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;

import bamboo.world.data.RegionManager;

public class Util {
    public static void rmTree(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
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
    }

    public static void rmTreeFilterRegion(MinecraftServer server, Path path) throws IOException {
        Files.walkFileTree(path, new FileVisitor(server, path) {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!includes(file)) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void cpTree(Path src, Path dst) throws IOException {
        if (Files.exists(src)) {
            rmTree(dst);
            Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.createDirectory(dst.resolve(src.relativize(dir).toString()));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, dst.resolve(src.relativize(file).toString()));
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static void cpTreeFilterRegion(MinecraftServer server, Path src, Path dst) throws IOException {
        rmTree(dst);
        Files.walkFileTree(src, new FileVisitor(server, src) {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                super.preVisitDirectory(dir, attrs);
                Files.createDirectory(dst.resolve(src.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (includes(file)) {
                    Files.copy(file, dst.resolve(src.relativize(file).toString()));
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static class FileVisitor extends SimpleFileVisitor<Path> {
        private List<RegionManager> stack = new ArrayList<>();
        private Map<Path, RegionManager> dimensions = new HashMap<>();

        public FileVisitor(MinecraftServer server, Path root) {
            server.getWorlds().forEach(world -> {
                dimensions.put(DimensionType.getSaveDirectory(world.getRegistryKey(), root), RegionManager.get(world));
            });
        }

        protected boolean includes(Path file) {
            String filename = file.getFileName().toString();
            if (filename.endsWith(".lock")) {
                return false;
            } else if (filename.endsWith(".mca")) {
                String[] parts = filename.split("\\.");
                int x = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);
                return stack.getLast().contains(x, z);
            }

            return true;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (dimensions.containsKey(dir)) {
                stack.addLast(dimensions.get(dir));
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
            if (dimensions.containsKey(dir)) {
                stack.removeLast();
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
