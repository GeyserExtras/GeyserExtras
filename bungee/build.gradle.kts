dependencies {
    implementation(project(":core"))
    compileOnly("net.kyori:adventure-api:4.18.0")
    compileOnly("org.geysermc.geyser:core:2.8.2-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
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
