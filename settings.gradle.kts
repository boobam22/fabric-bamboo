pluginManagement {
    val loom_version: String by settings

    plugins {
        id("fabric-loom") version loom_version
        id("com.gradleup.shadow") version "8.3.6"
    }

    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net")
    }
}

rootProject.name = "fabric-bamboo"

file("mods").listFiles().forEach { file ->
    if (file.name.startsWith("bamboo-")) {
        include(":${file.name}")
        project(":${file.name}").projectDir = file("mods/${file.name}")
    }
}