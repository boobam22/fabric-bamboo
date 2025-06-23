plugins {
    id("com.gradleup.shadow")
}

dependencies {
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
    input.set(tasks.shadowJar.get().archiveFile)
}