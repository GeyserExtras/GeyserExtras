
dependencies {
    implementation(project(":core"))
    compileOnly("org.spongepowered:configurate-yaml:4.2.0-GeyserMC-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.18.0")
    compileOnly("org.geysermc.geyser:core:2.9.0-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}


tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras-Spigot.jar")
        relocate("org.spongepowered", "dev.letsgoaway.relocate.org.spongepowered")
        relocate("io.leangen.geantyref", "dev.letsgoaway.io.leangen.geantyref")
        relocate("com.fasterxml", "dev.letsgoaway.relocate.com.fasterxml")
        relocate("net.kyori", "org.geysermc.geyser.platform.spigot.shaded.net.kyori") {
            exclude("net.kyori.adventure.text.logger.slf4j.ComponentLogger")
        }
        relocate("it.unimi.dsi.fastutil", "org.geysermc.geyser.platform.spigot.shaded.it.unimi.dsi.fastutil")
    }
}
