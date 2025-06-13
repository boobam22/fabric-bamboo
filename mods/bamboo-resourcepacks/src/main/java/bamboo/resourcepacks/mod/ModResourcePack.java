package bamboo.resourcepacks.mod;

import java.util.Set;
import java.util.Map;
import java.util.Optional;
import java.lang.Integer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;

import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.InputSupplier;
import net.minecraft.util.Identifier;

public class ModResourcePack extends AbstractFileResourcePack {
    private final String baseID;
    private final Map<ResourceType, Set<String>> namespaces;
    private final Map<String, Path> rootPaths;

    ModResourcePack(ResourcePackInfo info, String baseID, Map<ResourceType, Set<String>> namespaces,
            Map<String, Path> rootPaths) {
        super(info);
        this.baseID = baseID;
        this.namespaces = namespaces;
        this.rootPaths = rootPaths;
    }

    @Override
    public InputSupplier<InputStream> openRoot(String... segments) {
        if (segments.length == 1 && segments[0].equals("pack.png")) {
            return InputSupplier.create(this.rootPaths.get(this.baseID).resolve(segments[0]));
        }
        return null;
    }

    private Path toPath(ResourceType type, Identifier id) {
        return toPath(type, id.getNamespace(), id.getPath());
    }

    private Path toPath(ResourceType type, String namespace, String path) {
        if (this.namespaces.get(type).contains(namespace)) {
            Path target = this.rootPaths.get(namespace).resolve(type.getDirectory()).resolve(namespace).resolve(path);
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
    public void findResources(ResourceType type, String namespace, String prefix,
            ResourcePack.ResultConsumer consumer) {
        Optional.ofNullable(toPath(type, namespace, prefix)).ifPresent(root -> {
            try {
                Files.find(root, Integer.MAX_VALUE, (p, attributes) -> attributes.isRegularFile())
                        .forEach(path -> {
                            Identifier id = Identifier.of(namespace, prefix + "/" + root.relativize(path).toString());
                            consumer.accept(id, InputSupplier.create(path));
                        });
            } catch (IOException e) {
            }
        });
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return this.namespaces.get(type);
    }

    @Override
    public void close() {
    }
}
