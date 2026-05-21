
dependencies {
    val configurate_yaml: String by project
    val adventure_api_version: String by project
    val geyser_version: String by project
    val spigot_version: String by project
    val packetevents_version: String by project

    implementation(project(":core"))
    compileOnly("org.spongepowered:configurate-yaml:${configurate_yaml}")
    compileOnly("net.kyori:adventure-api:${adventure_api_version}")
    compileOnly("org.geysermc.geyser:core:${geyser_version}")
    compileOnly("org.spigotmc:spigot-api:${spigot_version}")
    compileOnly("com.github.retrooper:packetevents-spigot:${packetevents_version}")

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
