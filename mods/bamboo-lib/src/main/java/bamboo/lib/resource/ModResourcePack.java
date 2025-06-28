package bamboo.lib.resource;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.InputStream;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.InputSupplier;
import net.minecraft.util.Identifier;

public class ModResourcePack extends AbstractFileResourcePack {
    private final Map<String, Path> rootPaths;
    private final Map<ResourceType, Map<String, Path>> namespaces;

    ModResourcePack(ResourcePackInfo info) {
        super(info);
        rootPaths = new LinkedHashMap<>();
        namespaces = new HashMap<>(Map.of(
                ResourceType.CLIENT_RESOURCES, new HashMap<>(),
                ResourceType.SERVER_DATA, new HashMap<>()));

        FabricLoader.getInstance().getAllMods().forEach(mod -> {
            String id = mod.getMetadata().getId();

            if (id.startsWith("bamboo-")) {
                Path root = mod.getRootPaths().get(0);
                rootPaths.put(id, root);

                for (ResourceType type : ResourceType.values()) {
                    for (String namespace : Set.of(id, "minecraft")) {
                        Path path = root.resolve(type.getDirectory()).resolve(namespace);
                        if (Files.exists(path)) {
                            namespaces.get(type).put(namespace, path);
                        }
                    }
                }
            }
        });
    }

    @Override
    public InputSupplier<InputStream> openRoot(String... segments) {
        if (segments.length == 1 && segments[0].equals("pack.png")) {
            return InputSupplier.create(rootPaths.get(getInfo().id()).resolve(segments[0]));
        }
        return null;
    }

    private Path toPath(ResourceType type, Identifier id) {
        return toPath(type, id.getNamespace(), id.getPath());
    }

    private Path toPath(ResourceType type, String namespace, String path) {
        if (namespace.equals("minecraft")) {
            for (Path root : rootPaths.values()) {
                Path target = root.resolve(type.getDirectory()).resolve("minecraft").resolve(path);
                if (Files.exists(target)) {
                    return target;
                }
            }
        } else {
            Path target = namespaces.get(type).get(namespace).resolve(path);
            return Files.exists(target) ? target : null;
        }
        return null;
    }

    @Override
    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        return Optional.ofNullable(toPath(type, id))
                .map(InputSupplier::create)
                .orElse(null);
    }

    @Override
    public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
        Optional.ofNullable(toPath(type, namespace, prefix)).ifPresent(root -> {
            try {
                Files.find(root, Integer.MAX_VALUE, (path, attributes) -> attributes.isRegularFile()).forEach(path -> {
                    Identifier id = Identifier.of(namespace, prefix + "/" + root.relativize(path).toString());
                    consumer.accept(id, InputSupplier.create(path));
                });
            } catch (IOException e) {
            }
        });
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return namespaces.get(type).keySet();
    }

    @Override
    public void close() {
    }
}
