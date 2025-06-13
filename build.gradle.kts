import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project

val fabric_version: String by project

plugins {
    id("fabric-loom") apply false
}

allprojects {
    apply(plugin = "fabric-loom")

    dependencies {
        add("minecraft", "com.mojang:minecraft:$minecraft_version")
        add("mappings", "net.fabricmc:yarn:$yarn_mappings:v2")
        add("modImplementation", "net.fabricmc:fabric-loader:$loader_version")
    }

    extensions.getByType<JavaPluginExtension>().apply {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.named<ProcessResources>("processResources") {
        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "minecraft_version" to minecraft_version,
                "loader_version" to loader_version,
                "version" to project.version,
            ))
        }
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveBaseName.set("${project.name}-$yarn_mappings")
    }
}

subprojects {
    extensions.getByType<LoomGradleExtensionAPI>().runs {
        named("client") {
            runDir = "../../run"
            programArgs.addAll(listOf("--username", "player"))
        }
        named("server") {
            runDir = "../../run/server"
        }
    }
}

extensions.getByType<LoomGradleExtensionAPI>().runs {
    named("client") {
        runDir = "run"
        programArgs.addAll(listOf("--username", "player"))
    }
    named("server") {
        runDir = "run/server"
    }
}

extensions.getByType<LoomGradleExtensionAPI>().mixin {
    useLegacyMixinAp.set(false)
}

val jarsDir = layout.buildDirectory.dir("jars")
val collectJars = tasks.register<Copy>("collectJars") {
    group = "build"

    from(subprojects.map { it.tasks.named<RemapJarTask>("remapJar").get().archiveFile })
    into(jarsDir)
}

tasks.named<RemapJarTask>("remapJar") {
    dependsOn(collectJars)
    nestedJars.from(project.provider {
        jarsDir.get().asFile
            .listFiles()?.toList()
            ?:emptyList()
    })
}