plugins {
    id("com.gradleup.shadow")
}

loom {
    accessWidenerPath.set(file("src/main/resources/bamboo-inventory.accesswidener"))
}

dependencies {
    implementation(project(path=":bamboo-lib", configuration="namedElements"))
    implementation("com.belerweb:pinyin4j:2.5.0")
}

tasks.shadowJar {
    dependencies {
        include(dependency("com.belerweb:pinyin4j:2.5.0"))
    }
    relocate("net.sourceforge", "bamboo.inventory.shadow")
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    inputFile.set(tasks.shadowJar.flatMap { it.archiveFile })
}