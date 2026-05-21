dependencies {
    val adventure_api_version: String by project
    val geyser_version: String by project
    val bungeecord_version: String by project
    val packetevents_version: String by project

    implementation(project(":core"))
    compileOnly("net.kyori:adventure-api:${adventure_api_version}")
    compileOnly("org.geysermc.geyser:core:${geyser_version}")
    compileOnly("net.md-5:bungeecord-api:${bungeecord_version}")
    compileOnly("com.github.retrooper:packetevents-bungeecord:${packetevents_version}")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras-BungeeCord.jar")
        relocate("org.spongepowered", "dev.letsgoaway.relocate.org.spongepowered")
        relocate("io.leangen.geantyref", "dev.letsgoaway.io.leangen.geantyref")
        relocate("com.fasterxml", "dev.letsgoaway.relocate.com.fasterxml")
        relocate("net.kyori", "org.geysermc.geyser.platform.bungeecord.shaded.net.kyori")
    }
}
