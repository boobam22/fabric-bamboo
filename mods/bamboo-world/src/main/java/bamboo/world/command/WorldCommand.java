package bamboo.world.command;

import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.io.IOException;
import java.net.URI;

import net.fabricmc.loader.api.FabricLoader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import bamboo.lib.command.SimpleCommand;
import bamboo.lib.command.Decorator;

public class WorldCommand implements SimpleCommand {
    private static Path BACKUP;

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        new RegionCommand().register(dispatcher);
        new PointCommand().register(dispatcher);
        new TpCommand().register(dispatcher);

        dispatcher.register(literal("bb-world")
                .then(literal("backup").executes(backup))
                .then(literal("restore").executes(restore)
                        .then(argument("zip", StringArgumentType.string())
                                .suggests(findBackups)
                                .executes(restoreFromZip)))
                .then(literal("clean").executes(clean)));
    }

    private static AfterSave backup = (ctx, server, src, dst) -> {
        Util.cpTreeFilterRegion(server, src, dst);
    };

    private static AfterStop restore = (ctx, server, src, dst) -> {
        Util.cpTree(dst, src);
    };

    private static AfterStop restoreFromZip = (ctx, server, src, dst) -> {
        String fileName = StringArgumentType.getString(ctx, "zip");
        URI uri = URI.create("jar:" + BACKUP.resolve(fileName).toUri());
        try (FileSystem fs = FileSystems.newFileSystem(uri, Map.of())) {
            Path zip = fs.getPath("/").resolve(src.getFileName().toString());
            Util.cpTree(zip, src);
        }
    };

    private static AfterStop clean = (ctx, server, src, dst) -> {
        Util.rmTreeFilterRegion(server, src);
    };

    private static Decorator.BaseSuggestion findBackups = ctx -> {
        try {
            return Files.find(BACKUP, 1, (path, attrs) -> attrs.isRegularFile())
                    .map(path -> path.getFileName().toString()).toList();
        } catch (IOException e) {
            return List.of();
        }
    };

    private static interface AfterSave extends Decorator.Base {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) {
            MinecraftServer server = ctx.getSource().getServer();
            Path src = server.getSavePath(WorldSavePath.ROOT).normalize();
            Path dst = BACKUP.resolve(src.getFileName().toString());
            server.getWorlds().forEach(world -> {
                world.savingDisabled = true;
            });

            server.saveAll(true, true, true);

            new Thread(() -> {
                try {
                    handle(ctx, server, src, dst);
                } catch (IOException e) {
                } finally {
                    server.execute(() -> {
                        server.getWorlds().forEach(world -> {
                            world.savingDisabled = false;
                        });
                    });
                }
            }).start();

            return 1;
        }

        void handle(CommandContext<ServerCommandSource> ctx, MinecraftServer server, Path src, Path dst)
                throws IOException;
    }

    private static interface AfterStop extends Decorator.Base {
        @Override
        default int run(CommandContext<ServerCommandSource> ctx) {
            MinecraftServer server = ctx.getSource().getServer();
            Path src = server.getSavePath(WorldSavePath.ROOT).normalize();
            Path dst = BACKUP.resolve(src.getFileName().toString());

            new Thread(() -> {
                server.stop(true);
                try {
                    handle(ctx, server, src, dst);
                } catch (IOException e) {
                }
            }).start();

            return 1;
        }

        void handle(CommandContext<ServerCommandSource> ctx, MinecraftServer server, Path src, Path dst)
                throws IOException;
    }

    static {
        BACKUP = FabricLoader.getInstance().getGameDir().resolve("backups");
        if (!Files.exists(BACKUP)) {
            try {
                Files.createDirectories(BACKUP);
            } catch (IOException e) {
            }
        }
    }
}